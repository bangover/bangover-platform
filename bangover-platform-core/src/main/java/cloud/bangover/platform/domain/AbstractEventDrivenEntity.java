package cloud.bangover.platform.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public abstract class AbstractEventDrivenEntity<I, E extends AbstractEventDrivenEntity<I, E>>
    extends AbstractEntity<I> implements EventDrivenEntity<I, E> {
  @Getter
  private final EntityEventsManager<I, E> localEventsManager = new EntityEventsManager<>();

  public AbstractEventDrivenEntity(I id) {
    super(id);
  }

  protected final <V extends EntityEvent<I, E, V>> void publishDomainEvent(V event) {
    localEventsManager.publishDomainEvent(event);
  }
}
