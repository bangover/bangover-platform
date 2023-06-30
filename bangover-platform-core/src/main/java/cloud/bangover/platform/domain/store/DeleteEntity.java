package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.Entity;

/**
 * This interface describes the function for entity delete.
 * 
 * @param <I> The entity id type
 * @param <E> The entity type
 */
public interface DeleteEntity<I, E extends Entity<I>> {
  /**
   * Delete entity
   * 
   * @param id The entity id
   */
  void delete(I id);
}
