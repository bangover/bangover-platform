package cloud.bangover.errors;

import cloud.bangover.errors.ErrorDescriptor.ErrorSeverity;
import cloud.bangover.platform.domain.values.JaxbSafeAdapter;
import java.util.Optional;

public class JaxbErrorSeverityAdapter extends JaxbSafeAdapter<String, ErrorSeverity> {

  @Override
  protected Optional<ErrorSeverity> unmarshal(Optional<String> value) throws Exception {
    return value.map(ErrorSeverity::valueOf);
  }

  @Override
  protected Optional<String> marshal(Optional<ErrorSeverity> value) throws Exception {
    return value.map(ErrorSeverity::name);
  }
}
