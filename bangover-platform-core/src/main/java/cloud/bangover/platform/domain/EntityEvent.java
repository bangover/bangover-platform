package cloud.bangover.platform.domain;

import cloud.bangover.events.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityEvent<I, E, V extends EntityEvent<I, E, V>> {
  private final EventType<V> eventType;
  private final E entity;
}
