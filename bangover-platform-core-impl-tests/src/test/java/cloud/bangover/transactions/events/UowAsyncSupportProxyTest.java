package cloud.bangover.transactions.events;

import cloud.bangover.async.Async;
import cloud.bangover.transactions.UnitOfWork;
import cloud.bangover.transactions.UnitOfWorkContext;
import cloud.bangover.transactions.UowAsyncSupportProxy;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UowAsyncSupportProxyTest {
  @Test
  public void shouldCreateAsyncContext() {
    // Given
    UnitOfWork unitOfWork = new StubUnitOfWork();
    unitOfWork = new UowAsyncSupportProxy(unitOfWork);

    // Expect
    Optional<String> initialContext = Async.getController().getCurrentContextId();
    unitOfWork.executeWorkUnit(() -> {
      Optional<String> createdContext = Async.getController().getCurrentContextId();
      Assert.assertFalse(initialContext.isPresent());
      Assert.assertTrue(createdContext.isPresent());
    });
  }

  @Test
  public void shouldCreateAsyncContextOnAbort() {
    // Given
    UnitOfWork unitOfWork = new StubUnitOfWork();
    unitOfWork = new UowAsyncSupportProxy(unitOfWork);
    String expectedContext = Async.getController().createAsyncContext();
    unitOfWork.executeWorkUnit(() -> {
      Optional<String> currentContext = Async.getController().getCurrentContextId();
      Assert.assertEquals(expectedContext, currentContext.get());
    });
    // Cleanup
    Async.getController().destroyAsyncContext(expectedContext);
  }

  private static class StubUnitOfWork implements UnitOfWork {
    @Override
    public UnitOfWorkContext startWork() {
      return new StubUnitOfWorkContext();
    }
  }

  private static class StubUnitOfWorkContext implements UnitOfWorkContext {

    @Override
    public void completeWork() {
    }

    @Override
    public void abortWork() {

    }
  }
}
