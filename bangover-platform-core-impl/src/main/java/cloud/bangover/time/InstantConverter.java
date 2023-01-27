package cloud.bangover.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class InstantConverter {
  @Getter
  private final Instant instant;

  public static InstantConverter of(Date date) {
    return of(date.toInstant());
  }

  public static InstantConverter ofUtcDate(LocalDate date) {
    return ofUtcDateTime(date.atStartOfDay());
  }

  public static InstantConverter ofUtcDateTime(LocalDateTime dateTime) {
    return ofLocalDateTime(dateTime, ZoneOffset.UTC);
  }

  public static InstantConverter ofLocalDateTime(LocalDateTime dateTime, ZoneOffset zoneOffset) {
    return ofOffsetDateTime(dateTime.atOffset(zoneOffset));
  }

  public static InstantConverter ofOffsetDateTime(OffsetDateTime dateTime) {
    return ofZonedDateTime(dateTime.toZonedDateTime());
  }

  public static InstantConverter ofZonedDateTime(ZonedDateTime dateTime) {
    return of(dateTime.toInstant());
  }

  public LocalDate getUtcDate() {
    return getLocalDate(ZoneOffset.UTC);
  }

  public LocalTime getUtcTime() {
    return getLocalTime(ZoneOffset.UTC);
  }

  public LocalDateTime getUtcDateTime() {
    return getLocalDateTime(ZoneOffset.UTC);
  }

  public LocalDate getLocalDate(ZoneOffset zoneOffset) {
    return getLocalDateTime(zoneOffset).toLocalDate();
  }

  public LocalTime getLocalTime(ZoneOffset zoneOffset) {
    return getLocalDateTime(zoneOffset).toLocalTime();
  }

  public LocalDateTime getLocalDateTime(ZoneOffset zoneOffset) {
    return getZonedDateTime(zoneOffset).toLocalDateTime();
  }

  public ZonedDateTime getZonedDateTime(ZoneOffset zoneOffset) {
    return getOffsetDateTime(zoneOffset).toZonedDateTime();
  }

  public OffsetDateTime getOffsetDateTime(ZoneOffset zoneOffset) {
    return instant.atOffset(zoneOffset);
  }
}
