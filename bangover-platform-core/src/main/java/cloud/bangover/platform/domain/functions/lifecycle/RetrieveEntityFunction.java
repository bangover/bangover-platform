package cloud.bangover.platform.domain.functions.lifecycle;

import java.util.Optional;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.functions.EntityNotFoundException;
import cloud.bangover.platform.domain.store.RetrieveEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RetrieveEntityFunction<I, E extends Entity<I>>
    implements BusinessFunction<I, Optional<E>> {

  private final RetrieveEntity<I, E> retrieveEntityFunc;

  @Override
  public void invoke(Context<I, Optional<E>> context) {
    context.reply(retrieveEntityFunc.find(context.getRequest()));
  }

  public BusinessFunction<I, E> failOnEmptyResult() {
    return context -> {
      invoke(new Context<I, Optional<E>>() {

        @Override
        public I getRequest() {
          return context.getRequest();
        }

        @Override
        public void reply(Optional<E> response) {
          if (response.isPresent()) {
            context.reply(response.get());
          } else {
            reject(new EntityNotFoundException());
          }
        }

        @Override
        public void reject(Exception error) {
          context.reject(error);
        }
      });
    };
  }
}
