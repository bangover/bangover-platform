package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.platform.domain.store.DataContainer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class StoreActionContext<D, A> {
  private final DataContainer<D> dataContainer;
  private final A action;
}