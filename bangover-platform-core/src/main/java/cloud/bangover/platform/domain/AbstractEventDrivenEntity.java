package cloud.bangover.platform.domain;

import cloud.bangover.events.GlobalEvents;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public abstract class AbstractEventDrivenEntity<I, E extends AbstractEventDrivenEntity<I, E>>
    extends AbstractEntity<I> {

  public AbstractEventDrivenEntity(I id) {
    super(id);
  }

  protected final <V extends EntityEvent<I, E, V>> void publishDomainEvent(V event) {
    GlobalEvents.publisher(event.getEventType()).publish(event);
  }
}
