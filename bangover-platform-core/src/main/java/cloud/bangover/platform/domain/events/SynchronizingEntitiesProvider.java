package cloud.bangover.platform.domain.events;

import cloud.bangover.platform.domain.Entity;
import java.util.Collection;

public interface SynchronizingEntitiesProvider<I, E extends Entity<I>, V> {
  Collection<E> findBy(V event);
}