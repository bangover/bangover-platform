package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SynchronizingFunction<D, I, E extends Entity<I>> implements BusinessFunction<D, Void> {
  private final EntityStore<I, E> entityStore;
  private final SynchronizingConverter<D, SynchronizingCommand<I, E>> entityConverter;

  @Override
  public void invoke(Context<D, Void> context) {
    SynchronizingCommand<I, E> command = entityConverter.convert(context.getRequest());
    command.synchronize(entityStore);
    context.reply();
  }

  public interface SynchronizingConverter<D, C> {
    C convert(D data);
  }

  public interface SynchronizingCommand<I, E extends Entity<I>> {
    void synchronize(EntityStore<I, E> store);
  }

  @RequiredArgsConstructor
  public static class UpdateEntityCommand<I, E extends Entity<I>>
      implements SynchronizingCommand<I, E> {
    private final E entity;

    @Override
    public void synchronize(EntityStore<I, E> store) {
      store.save(entity);
    }
  }

  @RequiredArgsConstructor
  public static class RemoveEntityCommand<I, E extends Entity<I>>
      implements SynchronizingCommand<I, E> {
    private final I id;

    @Override
    public void synchronize(EntityStore<I, E> store) {
      store.delete(id);
    }
  }
}
