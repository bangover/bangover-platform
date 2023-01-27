package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction;
import cloud.bangover.platform.domain.store.EntityStore;

public class ImmutableEntityFunction<I, C extends EntityLifecycleFunction.EntityLifecycleCommand<I>,
    E extends Entity<I>, S> extends EntityLifecycleFunction<I, E, C, S> {
  private final RequestReplyLifecycleOperation<I, E, C, S> requestReplyOperation;

  public ImmutableEntityFunction(EntityStore<I, E> entityStore,
      RequestOnlyLifecycleOperation<I, E, C> entityOperation) {
    this(entityStore, (entity, command) -> {
      entityOperation.invoke(entity, command);
      return null;
    });
  }

  public ImmutableEntityFunction(EntityStore<I, E> entityStore,
      RequestReplyLifecycleOperation<I, E, C, S> requestReplyOperation) {
    super(entityStore);
    this.requestReplyOperation = requestReplyOperation;
  }

  @Override
  protected void processFoundEntity(Context<C, S> context, E entity) {
    context.reply(requestReplyOperation.invoke(entity, context.getRequest()));
  }
}
