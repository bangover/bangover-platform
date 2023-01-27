package cloud.bangover.time;

import java.time.Instant;

public class CurrentTimeProvider implements TimeProvider {
  @Override
  public Instant getCurrentInstant() {
    return Instant.now();
  }
}
