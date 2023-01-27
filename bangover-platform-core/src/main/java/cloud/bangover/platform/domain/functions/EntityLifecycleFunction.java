package cloud.bangover.platform.domain.functions;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntityLifecycleFunction<I, E extends Entity<I>,
    C extends EntityLifecycleFunction.EntityLifecycleCommand<I>, S>
    implements BusinessFunction<C, S> {
  protected final EntityStore<I, E> entityStore;

  @Override
  public void invoke(Context<C, S> context) {
    C command = context.getRequest();
    Optional<E> entity = entityStore.find(command.getId());
    if (entity.isPresent()) {
      processFoundEntity(context, entity.get());
    } else {
      processNotFoundEntity(context);
    }
  }

  protected abstract void processFoundEntity(Context<C, S> context, E entity);

  protected void processNotFoundEntity(Context<C, S> context) {
    context.reject(new EntityNotFoundException());
  }

  public interface EntityLifecycleCommand<I> {
    I getId();
  }

  public interface RequestReplyLifecycleOperation<I, E extends Entity<I>,
      C extends EntityLifecycleFunction.EntityLifecycleCommand<I>, S> {
    S invoke(E entity, C command);
  }

  public interface RequestOnlyLifecycleOperation<I, E extends Entity<I>,
      C extends EntityLifecycleCommand<I>> {
    void invoke(E entity, C command);
  }
}
