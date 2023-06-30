package cloud.bangover.platform.domain.functions;

import java.util.Optional;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.RetrieveEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntityLifecycleFunction<I, E extends Entity<I>,
    C extends EntityLifecycleFunction.EntityLifecycleCommand<I>, S>
    implements BusinessFunction<C, S> {
  protected final RetrieveEntity<I, E> retrieveEntityFunc;

  @Override
  public void invoke(Context<C, S> context) {
    C command = context.getRequest();
    Optional<E> entity = retrieveEntityFunc.find(command.getId());
    if (entity.isPresent()) {
      processFoundEntity(context, entity.get());
    } else {
      processNotFoundEntity(context);
    }
  }

  protected abstract void processFoundEntity(Context<C, S> context, E entity);

  /**
   * Process case when the entity was not found by id. The logic of this method may be overridden,
   * but the {@link Context} <b>MUST BE REJECTED OR RESOLVED</b>. Otherwise the function result will
   * be have non determined state.
   * 
   * @param context The {@link Context}
   */
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
