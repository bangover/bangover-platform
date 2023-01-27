package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.GlobalEvents;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GlobalEventsConfigurer {
  @Inject
  private EventBus.Factory eventBusFactory;

  public void configureEventBuses() {
    GlobalEvents.configureEventBus(eventBusFactory.createEventBus());
  }

  public void destroyEventBuses() {
    GlobalEvents.reset();
  }
}
