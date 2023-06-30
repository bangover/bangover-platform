package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction;
import cloud.bangover.platform.domain.store.DeleteEntity;
import cloud.bangover.platform.domain.store.EntityStore;
import cloud.bangover.platform.domain.store.RetrieveEntity;

public class DeleteEntityFunction<I, E extends Entity<I>,
    C extends EntityLifecycleFunction.EntityLifecycleCommand<I>>
    extends EntityLifecycleFunction<I, E, C, Void> {
  protected final DeleteEntity<I, E> deleteEntityFunc;

  public DeleteEntityFunction(EntityStore<I, E> entityStore) {
    this(entityStore, entityStore);
  }

  public DeleteEntityFunction(RetrieveEntity<I, E> retrieveEntityFunc,
      DeleteEntity<I, E> deleteEntityFunc) {
    super(retrieveEntityFunc);
    this.deleteEntityFunc = deleteEntityFunc;
  }

  @Override
  protected void processFoundEntity(Context<C, Void> context, E entity) {
    deleteEntityFunc.delete(entity.getId());
    context.reply(null);
  }
}
