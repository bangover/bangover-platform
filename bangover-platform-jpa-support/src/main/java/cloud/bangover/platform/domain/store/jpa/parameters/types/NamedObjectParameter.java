package cloud.bangover.platform.domain.store.jpa.parameters.types;

import javax.persistence.Query;

import cloud.bangover.platform.domain.store.jpa.parameters.Parameter;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NamedObjectParameter implements Parameter {
  @EqualsAndHashCode.Include
  private final String name;
  private final Object value;

  @Override
  public void apply(Query query) {
    query.setParameter(name, value);
  }
}
