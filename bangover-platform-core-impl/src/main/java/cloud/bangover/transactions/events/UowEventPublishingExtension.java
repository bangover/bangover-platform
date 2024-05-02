package cloud.bangover.transactions.events;

import cloud.bangover.transactions.UnitOfWorkExtension;
import cloud.bangover.transactions.events.UowEventsPublishingController.UowPublishingControllerProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UowEventPublishingExtension implements UnitOfWorkExtension {
  private final UowPublishingControllerProvider publishingControllerProvider;

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
    publishingControllerProvider.getPublishingController().cancel();
  }
}
