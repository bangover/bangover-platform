package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.events.GlobalEventsConfigurer;
import cloud.bangover.events.EventBus.EventSubscribtion;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class GlobalEventsLifecycleConfigurer {
  @Inject
  private EventBus.Factory eventBusFactory;

  @Inject
  private Instance<GlobalEventsConfigurer> eventsConfigurers;

  private List<EventSubscribtion> subscriptions = new ArrayList<>();

  public void configureEventBuses() {
    GlobalEvents.configureEventBus(eventBusFactory.createEventBus());
    for (GlobalEventsConfigurer eventsConfigurer : eventsConfigurers) {
      subscriptions.addAll(eventsConfigurer.configure().getSubscriptions());
    }
  }

  public void destroyEventBuses() {
    subscriptions.forEach(EventSubscribtion::unsubscribe);
    GlobalEvents.reset();
  }
}
