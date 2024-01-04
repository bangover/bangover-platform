package cloud.bangover.functions;

import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.timer.Timeout;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "createFor")
public class PlatformBusinessFunctionRegistry implements BusinessFunctionRegistry {
  private final BusinessFunctionRegistry internalRegistry;
  private Optional<BusinessFunctionDecorator> decorator = Optional.empty();

  @Override
  public <S> ReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction, Timeout timeout) {
    return internalRegistry.registerReplyOnlyFunction(responseType,
        decorateFunction(businessFunction), timeout);
  }

  @Override
  public <Q, S> RequestReplyInteractor<Q, S> registerRequestReplyFunction(
      Class<?> requestType, Class<?> responseType, BusinessFunction<Q, S> businessFunction,
      Timeout timeout) {
    return internalRegistry.registerRequestReplyFunction(requestType, responseType,
        decorateFunction(businessFunction), timeout);
  }

  public PlatformBusinessFunctionRegistry appendDecorator(BusinessFunctionDecorator decorator) {
    if (this.decorator.isPresent()) {
      this.decorator = this.decorator.map(original -> original.cascade(decorator));
    } else {
      this.decorator = Optional.of(decorator);
    }
    return this;
  }

  private <Q, S> BusinessFunction<Q, S> decorateFunction(BusinessFunction<Q, S> function) {
    return decorator.map(decorator -> decorator.decorate(function)).orElse(function);
  }
}
