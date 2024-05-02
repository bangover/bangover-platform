package cloud.bangover.transactions.events;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.EventDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UowEventsPublishingController {
  private final EventBus originalEventBus;
  private EventsPublishingMode publishingMode = new OutOfTransactionPublishingMode();

  public final void startTransaction() {
    this.publishingMode = new TransactionalPublishingMode();
  }

  public final void publish(EventDescriptor<Object> descriptor) {
    publishingMode.publish(descriptor);
  }

  public final void flush() {
    publishingMode.flush();
  }

  public final void cancel() {
    publishingMode.cancel();
  }

  private void resetEventPublishingMode() {
    this.publishingMode = new OutOfTransactionPublishingMode();
  }

  private interface EventsPublishingMode {
    void publish(EventDescriptor<Object> eventDescriptor);

    void flush();

    void cancel();
  }

  private class TransactionalPublishingMode implements EventsPublishingMode {
    private final Collection<EventDescriptor<Object>> events = new ArrayList<>();

    @Override
    public void publish(EventDescriptor<Object> eventDescriptor) {
      events.add(eventDescriptor);
    }

    @Override
    public void flush() {
      events.forEach(descriptor -> originalEventBus.getPublisher(descriptor.getEventType())
          .publish(descriptor.getEvent()));
      resetEventPublishingMode();
      events.clear();
    }

    @Override
    public void cancel() {
      resetEventPublishingMode();
      events.clear();
    }
  }

  private class OutOfTransactionPublishingMode implements EventsPublishingMode {
    @Override
    public void publish(EventDescriptor<Object> eventDescriptor) {
      originalEventBus.getPublisher(eventDescriptor.getEventType())
          .publish(eventDescriptor.getEvent());
      resetEventPublishingMode();
    }

    @Override
    public void flush() {
      resetEventPublishingMode();
    }

    @Override
    public void cancel() {
      resetEventPublishingMode();
    }
  }

  public interface UowPublishingControllerProvider {
    UowEventsPublishingController getPublishingController();
  }
}