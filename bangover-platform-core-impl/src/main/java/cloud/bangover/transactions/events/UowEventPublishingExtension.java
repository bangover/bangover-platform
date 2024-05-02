package cloud.bangover.transactions.events;

import cloud.bangover.transactions.UnitOfWorkExtension;
import cloud.bangover.transactions.events.UowEventsPublishingController.UowPublishingControllerProvider;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UowEventPublishingExtension implements UnitOfWorkExtension {
  private final UowPublishingControllerProvider publishingControllerProvider;
  private final PublishingOnAbortStrategy publishingOnAbortStrategy;

  public UowEventPublishingExtension(UowPublishingControllerProvider publishingControllerProvider) {
    this(publishingControllerProvider, PublishingOnAbortStrategy.FLUSH);
  }

  @Override
  public void onStarted() {
    publishingControllerProvider.getPublishingController().startTransaction();
  }

  @Override
  public void onCompleted() {
    publishingControllerProvider.getPublishingController().flush();
  }

  @Override
  public void onAborted() {
    publishingOnAbortStrategy.apply(publishingControllerProvider.getPublishingController());
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static enum PublishingOnAbortStrategy {
    FLUSH(ctrl -> ctrl.flush()),
    CANCEL(ctrl -> ctrl.cancel());

    private final Consumer<UowEventsPublishingController> actionStrategy;

    void apply(UowEventsPublishingController ctrl) {
      actionStrategy.accept(ctrl);
    }
  }
}
