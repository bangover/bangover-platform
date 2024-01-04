package cloud.bangover.events;

import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.events.EventBus.GlobalEventListener;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(staticName = "create")
public class GlobalEventsConfig {
  @Getter
  private final List<EventSubscribtion> subscriptions = new ArrayList<>();

  public <E> GlobalEventsConfig withSubscription(
      EventType<E> eventType, EventListener<E> eventListener) {
    subscriptions.add(GlobalEvents.subscribeOn(eventType, eventListener));
    return this;
  }

  public GlobalEventsConfig withGlobalSubscription(GlobalEventListener globalListener) {
    subscriptions.add(GlobalEvents.subscribeOnAll(globalListener));
    return this;
  }
}
