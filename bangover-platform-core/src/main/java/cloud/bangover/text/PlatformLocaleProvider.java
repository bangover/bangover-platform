package cloud.bangover.text;

import cloud.bangover.locale.LocaleProvider;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
public class PlatformLocaleProvider implements LocaleProvider {
  private final Iterable<LocaleExtractor> localeExtractors;

  @Override
  public Locale getLocale() {
    return extractLocale().orElse(Locale.getDefault());
  }

  private Optional<Locale> extractLocale() {
    for (LocaleExtractor localeExtractor : localeExtractors) {
      Optional<Locale> result = localeExtractor.extractLocale();
      if (result.isPresent()) {
        return result;
      }
    }
    return Optional.empty();
  }

  public interface LocaleExtractor {
    Optional<Locale> extractLocale();
  }
}
