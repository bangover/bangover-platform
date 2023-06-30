package cloud.bangover.platform.domain.store.jpa.parameters.types;

import java.util.Date;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import cloud.bangover.platform.domain.store.jpa.parameters.Parameter;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NamedTemporalParameter implements Parameter {
  @EqualsAndHashCode.Include
  private final String name;
  private final Date value;
  private final TemporalType type;

  @Override
  public void apply(Query query) {
    query.setParameter(name, value, type);
  }
}
