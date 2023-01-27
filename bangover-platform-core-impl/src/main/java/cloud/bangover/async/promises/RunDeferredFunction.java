package cloud.bangover.async.promises;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class RunDeferredFunction<T> {
  private final Deferred.DeferredFunction<T> deferredFunction;
  private final Deferred<T> deferred;
}
