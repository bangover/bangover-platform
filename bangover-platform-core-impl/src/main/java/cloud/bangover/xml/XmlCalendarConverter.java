package cloud.bangover.xml;

import cloud.bangover.errors.MustNeverBeHappenedError;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.*;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * This class is the converter legacy {@link XMLGregorianCalendar} date-time format to the new
 * java.time.* types.
 *
 * @author Dmitry Mikhaylenko
 */
@RequiredArgsConstructor(staticName = "of")
public class XmlCalendarConverter {
  @NonNull
  private final XMLGregorianCalendar xmlCalendar;

  /**
   * Create {@link XmlCalendarConverter} from the {@link Instant} object
   *
   * @param instant The {@link Instant} object
   * @return The {@link XmlCalendarConverter}
   */
  public static XmlCalendarConverter of(Instant instant) {
    ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    try {
      return of(
          DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(dateTime)));
    } catch (DatatypeConfigurationException error) {
      throw new MustNeverBeHappenedError(error);
    }
  }

  /**
   * Create {@link XmlCalendarConverter} from the {@link LocalDate} object
   *
   * @param localDate The {@link LocalDate} object
   * @return The {@link XmlCalendarConverter}
   */
  public static XmlCalendarConverter of(LocalDate localDate) {
    return of(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Create {@link XmlCalendarConverter} from the {@link LocalDateTime} object
   *
   * @param localDateTime The {@link LocalDateTime} object
   * @return The {@link XmlCalendarConverter}
   */
  public static XmlCalendarConverter of(LocalDateTime localDateTime) {
    return of(localDateTime.toLocalDate());
  }

  /**
   * Convert {@link LocalDate} to {@link XMLGregorianCalendar} null-safely.
   *
   * @param localDate The {@link Optional} of {@link LocalDate}
   * @return The {@link XMLGregorianCalendar}
   */
  public static Optional<XMLGregorianCalendar> localDateToCalendar(Optional<LocalDate> localDate) {
    return localDate.map(XmlCalendarConverter::of).map(XmlCalendarConverter::extract);
  }

  /**
   * Convert {@link LocalDate} to {@link XMLGregorianCalendar}.
   *
   * @param localDate The {@link LocalDate}
   * @return The {@link XMLGregorianCalendar}
   */
  public static XMLGregorianCalendar localDateToCalendar(LocalDate localDate) {
    return localDateToCalendar(Optional.ofNullable(localDate)).orElse(null);
  }

  /**
   * Convert {@link XMLGregorianCalendar} to {@link LocalDate} null-safely.
   *
   * @param calendar The {@link Optional} of {@link XMLGregorianCalendar}
   * @return The {@link Optional} of {@link LocalDate}
   */
  public static Optional<LocalDate> calendarToLocalDate(Optional<XMLGregorianCalendar> calendar) {
    return calendar.map(XmlCalendarConverter::of).map(XmlCalendarConverter::toLocalDate);
  }

  /**
   * Convert {@link XMLGregorianCalendar} to {@link LocalDate}.
   *
   * @param calendar The {@link XmlCalendarConverter}
   * @return The {@link LocalDate}
   */
  public static LocalDate calendarToLocalDate(XMLGregorianCalendar calendar) {
    return calendarToLocalDate(Optional.ofNullable(calendar)).orElse(null);
  }

  /**
   * Convert {@link LocalDateTime} to {@link XMLGregorianCalendar} null-safely.
   *
   * @param localDateTime The {@link Optional} of {@link LocalDateTime}
   * @return The {@link Optional} of {@link XMLGregorianCalendar}
   */
  public static Optional<XMLGregorianCalendar> localDateTimeToCalendar(
      Optional<LocalDateTime> localDateTime) {
    return localDateTime.map(XmlCalendarConverter::of).map(XmlCalendarConverter::extract);
  }

  /**
   * Convert {@link LocalDateTime} to {@link XMLGregorianCalendar}.
   *
   * @param localDateTime The {@link LocalDateTime}
   * @return The {@link XMLGregorianCalendar}
   */
  public static XMLGregorianCalendar localDateTimeToCalendar(LocalDateTime localDateTime) {
    return localDateTimeToCalendar(Optional.ofNullable(localDateTime)).orElse(null);
  }

  /**
   * Convert {@link XmlCalendarConverter} to {@link LocalDateTime} null-safely.
   *
   * @param calendar The {@link Optional} of {@link XMLGregorianCalendar}
   * @return The {@link Optional} of {@link LocalDateTime}
   */
  public static Optional<LocalDateTime> calendarToLocalDateTime(
      Optional<XMLGregorianCalendar> calendar) {
    return calendar.map(XmlCalendarConverter::of).map(XmlCalendarConverter::toLocalDateTime);
  }

  /**
   * Convert {@link XmlCalendarConverter} to {@link LocalDateTime}.
   *
   * @param calendar The {@link XMLGregorianCalendar}
   * @return The {@link LocalDateTime}
   */
  public static LocalDateTime calendarToLocalDateTime(XMLGregorianCalendar calendar) {
    return calendarToLocalDateTime(Optional.ofNullable(calendar)).orElse(null);
  }

  /**
   * Convert {@link Instant} to {@link XMLGregorianCalendar}.
   *
   * @param instant The {@link Instant}
   * @return The {@link XMLGregorianCalendar}
   */
  public static XMLGregorianCalendar instantToCalendar(Instant instant) {
    return Optional.ofNullable(instant).map(XmlCalendarConverter::of)
        .map(XmlCalendarConverter::extract).orElse(null);
  }

  /**
   * Convert {@link XMLGregorianCalendar} to {@link Instant}.
   *
   * @param calendar The {@link XMLGregorianCalendar}
   * @return The {@link Instant}
   */
  public static Instant calendarToInstant(XMLGregorianCalendar calendar) {
    return Optional.ofNullable(calendar).map(XmlCalendarConverter::of)
        .map(XmlCalendarConverter::toInstant).orElse(null);
  }

  /**
   * @return The {@link XMLGregorianCalendar}
   */
  public XMLGregorianCalendar extract() {
    return xmlCalendar;
  }

  /**
   * @return The {@link Instant}
   */
  public Instant toInstant() {
    return xmlCalendar.toGregorianCalendar().toInstant();
  }

  /**
   * @return The {@link LocalDate}
   */
  public LocalDate toLocalDate() {
    return toLocalDateTime().toLocalDate();
  }

  /**
   * @return The {@link LocalDateTime}
   */
  public LocalDateTime toLocalDateTime() {
    return LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault());
  }
}
