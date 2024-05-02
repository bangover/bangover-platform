package cloud.bangover.transactions.events;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.EventListener;
import cloud.bangover.events.EventPublisher;
import cloud.bangover.events.EventType;
import cloud.bangover.events.LocalEventBus;
import cloud.bangover.transactions.events.UowEventsPublishingController.UowPublishingControllerProvider;

public class UowEventBusProxy implements EventBus {
  private final EventBus originalEventBus;
  private final LocalEventBus localEventBus = new LocalEventBus();

  public UowEventBusProxy(EventBus originalEventBus,
      UowPublishingControllerProvider publishingControllerProvider) {
    super();
    this.originalEventBus = originalEventBus;
    localEventBus.subscribeOnAll(descriptor -> {
      publishingControllerProvider.getPublishingController().publish(descriptor);
    });
  }

  @Override
  public <E> EventPublisher<E> getPublisher(EventType<E> eventType) {
    return localEventBus.getPublisher(eventType);
  }

  @Override
  public <E> EventSubscribtion subscribeOn(EventType<E> eventType, EventListener<E> eventListener) {
    return originalEventBus.subscribeOn(eventType, eventListener);
  }

  @Override
  public EventSubscribtion subscribeOnAll(GlobalEventListener globalListener) {
    return originalEventBus.subscribeOnAll(globalListener);
  }
}
