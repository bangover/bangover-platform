package cloud.bangover.platform.config;

import cloud.bangover.transactions.JtaUnitOfWork;
import cloud.bangover.transactions.UnitOfWork;
import cloud.bangover.transactions.events.UowEventPublishingExtension;
import cloud.bangover.transactions.events.UowEventsPublishingController.UowPublishingControllerProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

@ApplicationScoped
public class UnitOfWorkConfiguration {
  @Inject
  private UserTransaction userTransaction;

  @Inject
  private UowPublishingControllerProvider uowPublishingControllerProvider;

  @ApplicationScoped
  public UnitOfWork unitOfWork() {
    JtaUnitOfWork result = new JtaUnitOfWork(userTransaction);
    result.addExtension(createEventPublishingExtension());
    return result;
  }

  private UowEventPublishingExtension createEventPublishingExtension() {
    return new UowEventPublishingExtension(uowPublishingControllerProvider);
  }
}
