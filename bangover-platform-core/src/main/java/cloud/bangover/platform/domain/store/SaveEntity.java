package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.Entity;

/**
 * This interface describes the function for entity save.
 * 
 * @param <I> The entity id type
 * @param <E> The entity type
 */
public interface SaveEntity<I, E extends Entity<I>> {
  /**
   * Save entity
   * 
   * @param entity
   */
  void save(E entity);
}
