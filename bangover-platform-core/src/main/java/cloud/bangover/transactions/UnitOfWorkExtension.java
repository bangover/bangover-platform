package cloud.bangover.transactions;

public interface UnitOfWorkExtension {
  default void onStarted() {
  }

  default void onCompleted() {
  }

  default void onAborted() {
  }
}
