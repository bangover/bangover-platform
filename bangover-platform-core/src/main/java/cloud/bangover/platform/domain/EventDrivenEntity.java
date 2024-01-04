package cloud.bangover.platform.domain;

public interface EventDrivenEntity<I, E extends EventDrivenEntity<I, E>> extends Entity<I> {
  EntityEventsManager<I, E> getLocalEventsManager();
}
