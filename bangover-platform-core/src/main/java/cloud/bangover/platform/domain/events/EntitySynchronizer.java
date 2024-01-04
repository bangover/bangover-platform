package cloud.bangover.platform.domain.events;

import cloud.bangover.platform.domain.Entity;

public interface EntitySynchronizer<I, E extends Entity<I>, V> {
  void synchronize(E entity, V event);
}
