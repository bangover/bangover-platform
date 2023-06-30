package cloud.bangover.platform.domain.functions;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.SaveEntity;

public class CreateEntityFunction<I, E extends Entity<I>, C> implements BusinessFunction<C, I> {
  private final SaveEntity<I, E> entitySaveFn;
  private final ParameterizedEntityFactory<I, E, C> entityCreator;

  /**
   * Create entity function for default entity factory.
   *
   * @param entitySaveFunc The entity save function
   * @param entityCreator  The entity creator
   */
  public CreateEntityFunction(SaveEntity<I, E> entitySaveFunc,
      DefaultEntityFactory<I, E> entityCreator) {
    this(entitySaveFunc, command -> entityCreator.createEntity());
  }

  /**
   * Create entity function for parameterized entity factory.
   *
   * @param entitySaveFunc The entity save function
   * @param entityCreator  The entity creator
   */
  public CreateEntityFunction(SaveEntity<I, E> entitySaveFunc,
      ParameterizedEntityFactory<I, E, C> entityCreator) {
    this.entitySaveFn = entitySaveFunc;
    this.entityCreator = entityCreator;
  }

  @Override
  public void invoke(Context<C, I> context) {
    C command = context.getRequest();
    E entity = entityCreator.createEntity(command);
    entitySaveFn.save(entity);
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
