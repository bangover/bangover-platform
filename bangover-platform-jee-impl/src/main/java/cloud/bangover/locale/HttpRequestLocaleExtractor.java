package cloud.bangover.locale;

import cloud.bangover.CollectionWrapper;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpRequestLocaleExtractor implements PlatformLocaleProvider.LocaleExtractor {
  private final HttpServletRequest httpServletRequest;

  @Override
  public Optional<Locale> extractLocale() {
    return CollectionWrapper.of(Collections.list(httpServletRequest.getLocales())).firstElement();
  }
}
