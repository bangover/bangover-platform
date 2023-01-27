package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RetrieveEntityFunction<I, E extends Entity<I>>
    implements BusinessFunction<I, Optional<E>> {
  private final EntityStore<I, E> entityStore;

  @Override
  public void invoke(Context<I, Optional<E>> context) {
    context.reply(entityStore.find(context.getRequest()));
  }
}
