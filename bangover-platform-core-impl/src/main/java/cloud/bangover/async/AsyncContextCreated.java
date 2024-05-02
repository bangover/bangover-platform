package cloud.bangover.async;

import cloud.bangover.events.EventType;

public class AsyncContextCreated extends AsyncContextEvent<AsyncContextCreated> {
  public AsyncContextCreated(String contextId) {
    super(contextId);
  }

  @Override
  public EventType<AsyncContextCreated> getType() {
    return ASYNC_CONTEXT_CREATED;
  }
}
