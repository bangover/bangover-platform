package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.Entity;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockEntityStore<I, E extends Entity<I>> extends MockDataContainer<E>
    implements EntityStore<I, E> {

  @Override
  public Optional<E> find(I id) {
    return Optional.ofNullable(getEntities().get(id));
  }

  @Override
  public void save(E entity) {
    getState().add(entity);
  }

  @Override
  public void delete(I id) {
    find(id).ifPresent(entity -> getState().remove(entity));
  }

  private Map<I, E> getEntities() {    
    return getState().stream().collect(Collectors.toMap(entity -> entity.getId(), entity -> entity));
  }
}
