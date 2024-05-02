package cloud.bangover.platform.domain;

import cloud.bangover.events.EventType;
import cloud.bangover.platform.domain.functions.EntityNotFoundException;
import cloud.bangover.platform.domain.store.RetrieveEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityEvent<I, E extends Entity<I>, V extends EntityEvent<I, E, V>> {
  private final EventType<V> eventType;
  private final I entityId;

  public E getEntity(RetrieveEntity<I, E> entityProvider) {
    return entityProvider.find(entityId).orElseThrow(EntityNotFoundException::new);
  }
}
