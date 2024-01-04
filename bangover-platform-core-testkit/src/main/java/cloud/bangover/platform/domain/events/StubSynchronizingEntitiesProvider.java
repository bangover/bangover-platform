package cloud.bangover.platform.domain.events;

import cloud.bangover.platform.domain.Entity;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class StubSynchronizingEntitiesProvider<I, E extends Entity<I>, V>
    implements SynchronizingEntitiesProvider<I, E, V> {
  private final Map<V, Set<E>> providingSets = new HashMap<>();

  public void configure(Consumer<StubConfigurer<I, E, V>> configurer) {
    configurer.accept(new StubConfigurer<I, E, V>() {
      @Override
      public StubConfigurer<I, E, V> withProvidedEntity(V event, E entity) {
        Set<E> entities = providingSets.computeIfAbsent(event, key -> new HashSet<>());
        entities.add(entity);
        return this;
      }
    });
  }

  public void clear() {
    providingSets.clear();
  }

  @Override
  public Collection<E> findBy(V event) {
    return providingSets.getOrDefault(event, Collections.emptySet());
  }

  public static interface StubConfigurer<I, E extends Entity<I>, V> {
    StubConfigurer<I, E, V> withProvidedEntity(V event, E entity);
  }
}
