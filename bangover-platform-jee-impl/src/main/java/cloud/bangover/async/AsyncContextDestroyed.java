package cloud.bangover.async;

import cloud.bangover.events.EventType;

public class AsyncContextDestroyed extends AsyncContextEvent<AsyncContextDestroyed> {
  public AsyncContextDestroyed(String contextId) {
    super(contextId);
  }

  @Override
  public EventType<AsyncContextDestroyed> getType() {
    return ASYNC_CONTEXT_DESTROYED;
  }
}
