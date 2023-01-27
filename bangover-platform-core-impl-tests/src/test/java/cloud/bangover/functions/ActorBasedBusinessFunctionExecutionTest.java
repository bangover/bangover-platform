package cloud.bangover.functions;

import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import cloud.bangover.actors.ActorSystem;
import cloud.bangover.actors.Actors;
import cloud.bangover.actors.CorrelationKeyGenerator;
import cloud.bangover.actors.SingleThreadDispatcher;
import cloud.bangover.async.promises.Promise;
import cloud.bangover.functions.registry.ActorSystemBusinessFunctionRegistry;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.interactions.interactor.WrongRequestTypeException;
import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.TimeoutException;
import lombok.RequiredArgsConstructor;

@RunWith(Theories.class)
public class ActorBasedBusinessFunctionExecutionTest {
  private static final Object REQUEST = 1L;
  private static final Object REPLY = 1L;
  private static final Exception ERROR = new Exception();
  private static final ReplyOnlyRunner REPLY_ONLY_RUNNER = new ReplyOnlyRunner();
  private static final RequestReplyRunner REQUEST_REPLY_RUNNER = new RequestReplyRunner(REQUEST);
  private static final RequestReplyRunner WRONG_TYPE_REQUEST_REPLY_RUNNER =
      new RequestReplyRunner("1");

  @DataPoints("testCases")
  public static Runnable[] testCases() {
    return new Runnable[] { createReplyOnlySuccessCase(), createRequestReplySuccessCase(),
        createReplyOnlyTimeoutCase(), createRequestReplyTimeoutCase(), createReplyOnlyErrorCase(),
        createRequestReplyErrorCase(), createRequestReplyWrongRequestTypeErrorCase() };
  }

  private static Runnable createReplyOnlySuccessCase() {
    BusinessFunction<Void, Object> function =
        new StubBusinessFunction<>(1L, Timeout.ofSeconds(10L));
    ReplyOnlyConfigurer configurer = new ReplyOnlyConfigurer(Long.class, function);
    SuccessResponseChecker checker = new SuccessResponseChecker(REPLY, Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, REPLY_ONLY_RUNNER, checker);
  }

  private static Runnable createRequestReplySuccessCase() {
    BusinessFunction<Object, Object> function = new EchoBusinessFunction<>(Timeout.ofSeconds(10L));
    RequestReplyConfigurer configurer =
        new RequestReplyConfigurer(Long.class, Long.class, function);
    SuccessResponseChecker checker = new SuccessResponseChecker(REPLY, Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, REQUEST_REPLY_RUNNER, checker);
  }

  private static Runnable createReplyOnlyTimeoutCase() {
    BusinessFunction<Void, Object> function =
        new StubBusinessFunction<>(1L, Timeout.ofSeconds(30L));
    ReplyOnlyConfigurer configurer =
        new ReplyOnlyConfigurer(Long.class, function, Timeout.ofSeconds(10L));
    ErrorResponseChecker checker = new ErrorResponseChecker(
        new TimeoutException(Timeout.ofSeconds(10L)), Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, REPLY_ONLY_RUNNER, checker);
  }

  private static Runnable createRequestReplyTimeoutCase() {
    BusinessFunction<Object, Object> function = new EchoBusinessFunction<>(Timeout.ofSeconds(30L));
    RequestReplyConfigurer configurer =
        new RequestReplyConfigurer(Long.class, Long.class, function, Timeout.ofSeconds(10L));
    ErrorResponseChecker checker = new ErrorResponseChecker(
        new TimeoutException(Timeout.ofSeconds(10L)), Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, REQUEST_REPLY_RUNNER, checker);
  }

  private static Runnable createReplyOnlyErrorCase() {
    BusinessFunction<Void, Object> function =
        new StubBusinessFunction<>(ERROR, Timeout.ofSeconds(10L));
    ReplyOnlyConfigurer configurer = new ReplyOnlyConfigurer(Long.class, function);
    ErrorResponseChecker checker = new ErrorResponseChecker(ERROR, Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, REPLY_ONLY_RUNNER, checker);
  }

  private static Runnable createRequestReplyErrorCase() {
    BusinessFunction<Object, Object> function =
        new StubBusinessFunction<>(ERROR, Timeout.ofSeconds(10L));
    RequestReplyConfigurer configurer =
        new RequestReplyConfigurer(Long.class, Long.class, function);
    ErrorResponseChecker checker = new ErrorResponseChecker(ERROR, Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, REQUEST_REPLY_RUNNER, checker);
  }

  private static Runnable createRequestReplyWrongRequestTypeErrorCase() {
    BusinessFunction<Object, Object> function = new EchoBusinessFunction<>(Timeout.ofSeconds(10L));
    RequestReplyConfigurer configurer =
        new RequestReplyConfigurer(Long.class, Long.class, function);
    ErrorResponseChecker checker = new ErrorResponseChecker(
        new WrongRequestTypeException(Long.class, "1"), Timeout.ofSeconds(30L));
    return new TestCase<>(configurer, WRONG_TYPE_REQUEST_REPLY_RUNNER, checker);
  }

  @Theory
  public void shouldAllTestCasesBePassed(@FromDataPoints("testCases") Runnable testCase) {
    testCase.run();
  }

  @RequiredArgsConstructor
  private static class TestCase<I> implements Runnable {
    private final CaseConfigurer<I> caseConfigurer;
    private final CaseRunner<I> caseRunner;
    private final ResultChecker resultChecker;

    @Override
    public void run() {
      ActorSystem actorSystem = Actors.create(systemConfigurer -> systemConfigurer
          .withCorrelationKeyGenerator(new CorrelationKeyGenerator("INSTANCE"))
          .withDispatcher(new SingleThreadDispatcher()).configure());
      try {
        actorSystem.start();
        BusinessFunctionRegistry businessFunctionRegistry =
            new ActorSystemBusinessFunctionRegistry(actorSystem);
        I interactor = caseConfigurer.configure(businessFunctionRegistry);
        Promise<Object> result = caseRunner.run(interactor);
        resultChecker.check(result);
      } catch (Throwable error) {
        Assert.fail();
      } finally {
        actorSystem.shutdown();
      }
    }
  }

  private interface CaseConfigurer<I> {
    I configure(BusinessFunctionRegistry registry);
  }

  @RequiredArgsConstructor
  private static class ReplyOnlyConfigurer implements CaseConfigurer<ReplyOnlyInteractor<Object>> {
    @SuppressWarnings("rawtypes")
    private final Class responseType;
    private final BusinessFunction<Void, Object> function;
    private final Timeout timeout;

    @SuppressWarnings("rawtypes")
    public ReplyOnlyConfigurer(Class responseType, BusinessFunction<Void, Object> function) {
      this(responseType, function, null);
    }

    @Override
    public ReplyOnlyInteractor<Object> configure(BusinessFunctionRegistry registry) {
      if (timeout != null) {
        return registry.registerReplyOnlyFunction(responseType, function, timeout);
      }
      return registry.registerReplyOnlyFunction(responseType, function);
    }
  }

  @RequiredArgsConstructor
  private static class RequestReplyConfigurer
      implements CaseConfigurer<RequestReplyInteractor<Object, Object>> {
    @SuppressWarnings("rawtypes")
    private final Class requestType;
    @SuppressWarnings("rawtypes")
    private final Class responseType;
    private final BusinessFunction<Object, Object> function;
    private final Timeout timeout;

    @SuppressWarnings("rawtypes")
    public RequestReplyConfigurer(Class requestType, Class responseType,
        BusinessFunction<Object, Object> function) {
      this(requestType, responseType, function, null);
    }

    @Override
    public RequestReplyInteractor<Object, Object> configure(BusinessFunctionRegistry registry) {
      if (timeout != null) {
        return registry.registerRequestReplyFunction(requestType, responseType, function, timeout);
      }
      return registry.registerRequestReplyFunction(requestType, responseType, function);
    }
  }

  private interface CaseRunner<I> {
    Promise<Object> run(I interactor);
  }

  private static class ReplyOnlyRunner implements CaseRunner<ReplyOnlyInteractor<Object>> {
    @Override
    public Promise<Object> run(ReplyOnlyInteractor<Object> interactor) {
      return interactor.invoke();
    }
  }

  @RequiredArgsConstructor
  private static class RequestReplyRunner
      implements CaseRunner<RequestReplyInteractor<Object, Object>> {
    private final Object request;

    @Override
    public Promise<Object> run(RequestReplyInteractor<Object, Object> interactor) {
      return interactor.invoke(request);
    }
  }

  private interface ResultChecker {
    void check(Promise<Object> result) throws Throwable;
  }

  @RequiredArgsConstructor
  private static class SuccessResponseChecker implements ResultChecker {
    private final Object expectedResponse;
    private final Timeout waitingTimeout;

    @Override
    public void check(Promise<Object> result) throws Throwable {
      Object actual = result.get(waitingTimeout.getMilliseconds().longValue());
      Assert.assertEquals(expectedResponse, actual);
    }
  }

  @RequiredArgsConstructor
  private static class ErrorResponseChecker implements ResultChecker {
    private final Throwable expectedThrowable;
    private final Timeout waitingTimeout;

    @Override
    public void check(Promise<Object> result) throws Throwable {
      Throwable throwable = Assert.assertThrows(expectedThrowable.getClass(), () -> {
        result.get(waitingTimeout.getMilliseconds());
      });
      Assert.assertEquals(expectedThrowable.getClass(), throwable.getClass());
      Assert.assertEquals(expectedThrowable.getMessage(), throwable.getMessage());
    }
  }
}
