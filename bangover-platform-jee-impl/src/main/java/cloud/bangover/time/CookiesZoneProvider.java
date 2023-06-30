package cloud.bangover.time;

import cloud.bangover.CollectionWrapper;
import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CookiesZoneProvider implements ZoneProvider {
  private static final String BROWSER_TIMEZONE_COOKIE_NAME = "BrowserTimeZone";

  private final HttpServletRequest request;

  @Override
  public ZoneOffset getZoneOffset() {
    Collection<Cookie> cookies = Optional.ofNullable(request).map(HttpServletRequest::getCookies)
        .map(Arrays::asList).orElse(Collections.emptyList());
    return CollectionWrapper.of(cookies)
        .find(cookie -> BROWSER_TIMEZONE_COOKIE_NAME.equals(cookie.getName())).map(cookie -> {
          try {
            return ZoneOffset.of(cookie.getValue());
          } catch (DateTimeException error) {
            return ZoneOffset.UTC;
          }
        }).orElse(ZoneOffset.UTC);
  }
}
