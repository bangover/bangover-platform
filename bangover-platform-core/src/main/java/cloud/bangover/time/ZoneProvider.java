package cloud.bangover.time;

import java.time.ZoneOffset;

public interface ZoneProvider {
  ZoneOffset getZoneOffset();
}
