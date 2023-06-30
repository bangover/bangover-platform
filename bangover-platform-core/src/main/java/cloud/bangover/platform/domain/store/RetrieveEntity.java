package cloud.bangover.platform.domain.store;

import java.util.Optional;

import cloud.bangover.platform.domain.Entity;

/**
 * This interface describes the function for entity retrieve by id.
 * 
 * @param <I> The entity id type
 * @param <E> The entity type
 */
public interface RetrieveEntity<I, E extends Entity<I>> {
  /**
   * Retrieve entity
   * 
   * @param id The entity id
   * @return The found entity, 
   */
  Optional<E> find(I id);
}
