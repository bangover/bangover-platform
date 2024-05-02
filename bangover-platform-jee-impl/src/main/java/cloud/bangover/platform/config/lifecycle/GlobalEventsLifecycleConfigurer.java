package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.events.GlobalEventsConfigurer;
import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.transactions.events.UowEventBusProxy;
import cloud.bangover.transactions.events.UowEventsPublishingController.UowPublishingControllerProvider;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class GlobalEventsLifecycleConfigurer {
  @Inject
  private EventBus defaultEventBus;

  @Inject
  private Instance<GlobalEventsConfigurer> eventsConfigurers;

  @Inject
  private UowPublishingControllerProvider uowPublishingControllerProvider;

  private List<EventSubscribtion> subscriptions = new ArrayList<>();

  public void configureEventBuses() {
    configureEventBus();
    for (GlobalEventsConfigurer eventsConfigurer : eventsConfigurers) {
      subscriptions.addAll(eventsConfigurer.configure().getSubscriptions());
    }
  }

  public void destroyEventBuses() {
    subscriptions.forEach(EventSubscribtion::unsubscribe);
    GlobalEvents.reset();
  }

  private void configureEventBus() {
    EventBus eventBus = new UowEventBusProxy(defaultEventBus, uowPublishingControllerProvider);
    GlobalEvents.configureEventBus(eventBus);
  }
}
