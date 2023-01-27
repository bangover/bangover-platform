package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction;
import cloud.bangover.platform.domain.store.EntityStore;

public class MutableEntityFunction<I, E extends Entity<I>,
    C extends EntityLifecycleFunction.EntityLifecycleCommand<I>, S>
    extends EntityLifecycleFunction<I, E, C, S> {
  private final RequestReplyLifecycleOperation<I, E, C, S> entityOperation;

  public MutableEntityFunction(EntityStore<I, E> entityStore,
      RequestOnlyLifecycleOperation<I, E, C> entityOperation) {
    this(entityStore, (entity, command) -> {
      entityOperation.invoke(entity, command);
      return null;
    });
  }

  public MutableEntityFunction(EntityStore<I, E> entityStore,
      RequestReplyLifecycleOperation<I, E, C, S> entityOperation) {
    super(entityStore);
    this.entityOperation = entityOperation;
  }

  @Override
  protected void processFoundEntity(Context<C, S> context, E entity) {
    S response = entityOperation.invoke(entity, context.getRequest());
    entityStore.save(entity);
    context.reply(response);
  }
}
