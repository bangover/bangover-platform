package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction;
import cloud.bangover.platform.domain.store.EntityStore;
import cloud.bangover.platform.domain.store.RetrieveEntity;
import cloud.bangover.platform.domain.store.SaveEntity;

public class MutableEntityFunction<I, E extends Entity<I>,
    C extends EntityLifecycleFunction.EntityLifecycleCommand<I>, S>
    extends EntityLifecycleFunction<I, E, C, S> {
  private final RequestReplyLifecycleOperation<I, E, C, S> entityOperation;
  protected final SaveEntity<I, E> saveEntityFunc;

  public MutableEntityFunction(EntityStore<I, E> entityStore,
      RequestOnlyLifecycleOperation<I, E, C> entityOperation) {
    this(entityStore, entityStore, entityOperation);
  }

  public MutableEntityFunction(RetrieveEntity<I, E> retrieveEntityFunc,
      SaveEntity<I, E> saveEntityFunc, RequestOnlyLifecycleOperation<I, E, C> entityOperation) {
    this(retrieveEntityFunc, saveEntityFunc, (entity, command) -> {
      entityOperation.invoke(entity, command);
      return null;
    });
  }

  public MutableEntityFunction(EntityStore<I, E> entityStore,
      RequestReplyLifecycleOperation<I, E, C, S> entityOperation) {
    this(entityStore, entityStore, entityOperation);
  }

  public MutableEntityFunction(RetrieveEntity<I, E> retrieveEntityFunc,
      SaveEntity<I, E> saveEntityFunc, RequestReplyLifecycleOperation<I, E, C, S> entityOperation) {
    super(retrieveEntityFunc);
    this.entityOperation = entityOperation;
    this.saveEntityFunc = saveEntityFunc;
  }

  @Override
  protected void processFoundEntity(Context<C, S> context, E entity) {
    S response = entityOperation.invoke(entity, context.getRequest());
    saveEntityFunc.save(entity);
    context.reply(response);
  }
}
