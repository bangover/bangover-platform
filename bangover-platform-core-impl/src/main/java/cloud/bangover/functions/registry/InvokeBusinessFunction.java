package cloud.bangover.functions.registry;

import cloud.bangover.async.promises.Deferred;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvokeBusinessFunction<Q, S> {
  private final Q request;
  private final Deferred<S> deferred;
}
