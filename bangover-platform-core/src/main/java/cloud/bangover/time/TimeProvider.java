package cloud.bangover.time;

import java.time.Instant;

public interface TimeProvider {
  Instant getCurrentInstant();
}
