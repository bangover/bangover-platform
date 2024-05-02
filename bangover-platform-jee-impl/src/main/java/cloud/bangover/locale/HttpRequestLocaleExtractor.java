package cloud.bangover.locale;

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
    return Collections.list(httpServletRequest.getLocales()).stream().findFirst();
  }
}
