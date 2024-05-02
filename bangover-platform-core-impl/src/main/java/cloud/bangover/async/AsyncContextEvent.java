package cloud.bangover.async;

import cloud.bangover.events.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class AsyncContextEvent<E extends AsyncContextEvent<E>> {
  public static final EventType<AsyncContextCreated> ASYNC_CONTEXT_CREATED =
      EventType.createFor("events.async.context-created", AsyncContextCreated.class);
  public static final EventType<AsyncContextDestroyed> ASYNC_CONTEXT_DESTROYED =
      EventType.createFor("events.async.context-destroyed", AsyncContextDestroyed.class);

  private final String contextId;

  public abstract EventType<E> getType();
}
