package cloud.bangover.platform.domain.store.jpa.parameters;

import javax.persistence.Query;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ObjectParameter implements Parameter {
  @EqualsAndHashCode.Include
  private final String name;
  private final Object value;

  @Override
  public void apply(Query query) {
    query.setParameter(name, value);
  }
}
