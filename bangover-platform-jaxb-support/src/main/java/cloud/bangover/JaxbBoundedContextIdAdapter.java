package cloud.bangover;

import cloud.bangover.platform.domain.values.JaxbSafeAdapter;
import java.util.Optional;

public class JaxbBoundedContextIdAdapter extends JaxbSafeAdapter<String, BoundedContextId> {
  @Override
  protected Optional<BoundedContextId> unmarshal(Optional<String> value) throws Exception {
    return value.map(val -> {
      if (!BoundedContextId.PLATFORM_CONTEXT.toString().equals(val)) {
        return BoundedContextId.createFor(val);
      }
      return BoundedContextId.PLATFORM_CONTEXT;
    });
  }

  @Override
  protected Optional<String> marshal(Optional<BoundedContextId> value) throws Exception {
    return value.map(Object::toString);
  }
}
