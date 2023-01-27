package cloud.bangover.platform.domain.store.jpa.parameters;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Query;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Parameters {
  private Set<Parameter> parameters = new HashSet<>();

  private Parameters(Parameters proto) {
    super();
    this.parameters.addAll(proto.parameters);
  }

  public void apply(Query query) {
    parameters.forEach(parameter -> parameter.apply(query));
  }

  public Parameters appendParameter(Parameter parameter) {
    Parameters result = new Parameters(this);
    result.parameters.add(parameter);
    return result;
  }

  public Parameters merge(Parameters parameters) {
    Parameters result = new Parameters(this);
    result.parameters.addAll(parameters.parameters);
    return result;
  }
}
