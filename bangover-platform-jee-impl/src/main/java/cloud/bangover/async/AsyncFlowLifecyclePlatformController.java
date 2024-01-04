package cloud.bangover.async;

import cloud.bangover.async.AsyncContext.LifecycleController;
import cloud.bangover.events.EventPublisher;
import cloud.bangover.events.EventType;
import cloud.bangover.events.GlobalEvents;
import java.util.Optional;

public class AsyncFlowLifecyclePlatformController implements LifecycleController {
  @Override
  public Optional<String> getCurrentContextId() {
    return getController().getCurrentContextId();
  }

  @Override
  public void restoreCurrentContextId(Optional<String> contextId) {
    getController().restoreCurrentContextId(contextId);
  }

  @Override
  public AsyncContext getCurrentContext() {
    return getController().getCurrentContext();
  }

  @Override
  public String createAsyncContext() {
    String key = getController().createAsyncContext();
    getPublisher(AsyncContextEvent.ASYNC_CONTEXT_CREATED).publish(new AsyncContextCreated(key));
    return key;
  }

  @Override
  public void destroyAsyncContext(String key) {
    getController().destroyAsyncContext(key);
    getPublisher(AsyncContextCreated.ASYNC_CONTEXT_DESTROYED)
        .publish(new AsyncContextDestroyed(key));
  }

  private <E> EventPublisher<E> getPublisher(EventType<E> type) {
    return GlobalEvents.publisher(type);
  }

  private LifecycleController getController() {
    return Async.getController();
  }
}
