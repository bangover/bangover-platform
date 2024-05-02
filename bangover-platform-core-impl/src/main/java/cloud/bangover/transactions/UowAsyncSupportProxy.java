package cloud.bangover.transactions;

import cloud.bangover.async.AsyncFlowLifecyclePlatformController;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class UowAsyncSupportProxy implements UnitOfWork {
  private static final AsyncFlowLifecyclePlatformController ASYNC_FLOW_CONTROLLER =
      new AsyncFlowLifecyclePlatformController();
  private final UnitOfWork original;

  @Override
  public UnitOfWorkContext startWork() {
    return original.startWork();
  }

  @Override
  public <S> S executeWorkUnit(Callable<S> handler) {
    if (ASYNC_FLOW_CONTROLLER.getCurrentContextId().isPresent()) {
      return executeInsideExistingContext(handler);
    }
    return executeInsideNewContext(handler);
  }

  private <S> S executeInsideNewContext(Callable<S> handler) {
    String newContext = ASYNC_FLOW_CONTROLLER.createAsyncContext();
    try {
      return executeInsideExistingContext(handler);
    } finally {
      ASYNC_FLOW_CONTROLLER.destroyAsyncContext(newContext);
    }
  }

  @SneakyThrows
  private <S> S executeInsideExistingContext(Callable<S> handler) {
    return original.executeWorkUnit(handler);
  }
}
