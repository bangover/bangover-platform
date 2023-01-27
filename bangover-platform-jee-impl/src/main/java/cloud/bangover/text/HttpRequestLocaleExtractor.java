package cloud.bangover.text;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class HttpRequestLocaleExtractor implements PlatformLocaleProvider.LocaleExtractor {
  @Inject
  private HttpServletRequest httpServletRequest;

  @Override
  public Optional<Locale> extractLocale() {
    return Optional.ofNullable(httpServletRequest.getLocale());
  }
}
