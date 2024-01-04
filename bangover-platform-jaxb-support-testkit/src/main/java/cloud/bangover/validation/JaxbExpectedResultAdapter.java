package cloud.bangover.validation;

import cloud.bangover.platform.domain.values.JaxbSafeAdapter;
import cloud.bangover.validation.ValidationCase.ExpectedResult;
import java.util.Optional;

public class JaxbExpectedResultAdapter extends JaxbSafeAdapter<String, ExpectedResult> {
  @Override
  protected Optional<ExpectedResult> unmarshal(Optional<String> value) throws Exception {
    return value.map(ExpectedResult::valueOf);
  }

  @Override
  protected Optional<String> marshal(Optional<ExpectedResult> value) throws Exception {
    return value.map(ExpectedResult::name);
  }
}
