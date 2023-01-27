package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction;
import cloud.bangover.platform.domain.store.EntityStore;

public class DeleteEntityFunction<I, E extends Entity<I>,
    C extends EntityLifecycleFunction.EntityLifecycleCommand<I>>
    extends EntityLifecycleFunction<I, E, C, Void> {
  public DeleteEntityFunction(EntityStore<I, E> entityStore) {
    super(entityStore);
  }

  @Override
  protected void processFoundEntity(Context<C, Void> context, E entity) {
    entityStore.delete(entity.getId());
    context.reply(null);
  }
}
