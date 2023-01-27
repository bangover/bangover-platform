package cloud.bangover.functions;

import cloud.bangover.StubbingQueue;
import cloud.bangover.async.promises.Promise;
import cloud.bangover.async.timer.Timeout;
import cloud.bangover.async.timer.Timer;

public class StubBusinessFunction<Q, S> implements BusinessFunction<Q, S> {
  private final StubbingQueue<Promise<S>> defaultStubbingQueue;
  private final Timeout sleepingTime;

  public StubBusinessFunction(Promise<S> defaultResponse, Timeout sleepingTime) {
    this.defaultStubbingQueue = new StubbingQueue<>(defaultResponse);
    this.sleepingTime = sleepingTime;
  }

  public StubbingQueue.StubbingQueueConfigurer<Promise<S>> configurer() {
    return defaultStubbingQueue.configure();
  }

  @Override
  public void invoke(Context<Q, S> context) {
    Promise<S> promise = defaultStubbingQueue.peek();
    Timer.sleep(sleepingTime);
    promise.then(context::reply).error(context::reject);
  }
}
