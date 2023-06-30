package cloud.bangover.platform.domain.store.jpa.parameters;

import javax.persistence.Query;

public interface Parameter {
  void apply(Query query);
}
