package cloud.bangover.platform.domain.functions;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;

public class CreateEntityFunction<I, E extends Entity<I>, C> implements BusinessFunction<C, I> {
  private final EntityStore<I, E> entityStore;
  private final ParameterizedEntityFactory<I, E, C> entityCreator;

  /**
   * Create entity function for default entity factory.
   *
   * @param entityStore   The entity store
   * @param entityCreator The entity creator
   */
  public CreateEntityFunction(EntityStore<I, E> entityStore,
      DefaultEntityFactory<I, E> entityCreator) {
    this(entityStore, command -> entityCreator.createEntity());
  }

  /**
   * Create entity function for parameterized entity factory.
   *
   * @param entityStore   The entity store
   * @param entityCreator The entity creator
   */
  public CreateEntityFunction(EntityStore<I, E> entityStore,
      ParameterizedEntityFactory<I, E, C> entityCreator) {
    this.entityStore = entityStore;
    this.entityCreator = entityCreator;
  }

  @Override
  public void invoke(Context<C, I> context) {
    C command = context.getRequest();
    E entity = entityCreator.createEntity(command);
    entityStore.save(entity);
    context.reply(entity.getId());
  }

  public interface DefaultEntityFactory<I, E extends Entity<I>> {
    /**
     * Create an entity.
     *
     * @return The created entity
     */
    E createEntity();
  }

  public interface ParameterizedEntityFactory<I, E extends Entity<I>, C> {
    /**
     * Create an entity.
     *
     * @param command The command object
     * @return The created entity
     */
    E createEntity(C command);
  }
}
