package cloud.bangover.platform.events;

import cloud.bangover.async.Async;
import cloud.bangover.async.AsyncContext;
import cloud.bangover.events.EventBus;
import cloud.bangover.transactions.events.UowEventsPublishingController;
import cloud.bangover.transactions.events.UowEventsPublishingController.UowPublishingControllerProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AsyncUowPublishingControllerProvider implements UowPublishingControllerProvider {
  private final EventBus defaultEventBus;
  private final DefaultPublishingControllerThreadLocal defaultPublishingController =
      new DefaultPublishingControllerThreadLocal();

  @Inject
  public AsyncUowPublishingControllerProvider(EventBus defaultEventBus) {
    super();
    this.defaultEventBus = defaultEventBus;
  }

  @Override
  public UowEventsPublishingController getPublishingController() {
    AsyncContext asyncContext = Async.getCurrentContext();
    UowEventsPublishingController controller = getControllerFromContext(asyncContext);
    asyncContext.attribute(getControllerAttributeKey(), controller);
    return controller;
  }

  private UowEventsPublishingController getControllerFromContext(AsyncContext context) {
    return context.attribute(getControllerAttributeKey())
        .map(UowEventsPublishingController.class::cast).orElseGet(defaultPublishingController::get);
  }

  private String getControllerAttributeKey() {
    return UowEventsPublishingController.class.getName();
  }

  private class DefaultPublishingControllerThreadLocal
      extends ThreadLocal<UowEventsPublishingController> {
    @Override
    protected UowEventsPublishingController initialValue() {
      return new UowEventsPublishingController(defaultEventBus);
    }
  }
}