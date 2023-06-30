package cloud.bangover.transactions;

import java.util.concurrent.Callable;
import lombok.SneakyThrows;

public interface UnitOfWork {
  default void executeWorkUnit(Runnable handler) {
    executeWorkUnit(() -> {
      handler.run();
      return null;
    });
  }

  @SneakyThrows
  default <S> S executeWorkUnit(Callable<S> handler) {
    UnitOfWorkContext context = startWork();
    try {
      S result = handler.call();
      context.completeWork();
      return result;
    } catch (Exception error) {
      context.abortWork();
      throw error;
    }
  }

  UnitOfWorkContext startWork();
}
