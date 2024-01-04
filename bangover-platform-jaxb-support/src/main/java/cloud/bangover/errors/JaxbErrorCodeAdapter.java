package cloud.bangover.errors;

import cloud.bangover.errors.ErrorDescriptor.ErrorCode;
import cloud.bangover.platform.domain.values.JaxbSafeAdapter;
import java.util.Optional;

public class JaxbErrorCodeAdapter extends JaxbSafeAdapter<Long, ErrorCode> {
  @Override
  protected Optional<ErrorCode> unmarshal(Optional<Long> value) throws Exception {
    return value.map(ErrorCode::createFor);
  }

  @Override
  protected Optional<Long> marshal(Optional<ErrorCode> value) throws Exception {
    return value.map(ErrorCode::extract);
  }
}
