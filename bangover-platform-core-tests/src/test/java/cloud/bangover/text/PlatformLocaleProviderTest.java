package cloud.bangover.text;

import cloud.bangover.locale.LocaleProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class PlatformLocaleProviderTest {
  private static final Locale PROVIDED_LOCALE = new Locale("ru_RU");

  @Theory
  public void shouldLocationsBeProvidedCorrectly(
      @FromDataPoints("extractLocalesCases") Runnable task) {
    task.run();
  }

  @DataPoints("extractLocalesCases")
  public static Runnable[] extractLocalesCases() {
    return new Runnable[] { new ExtractLocaleCase(Collections.emptyList(), Locale.getDefault()),
        new ExtractLocaleCase(Arrays.asList(new StubLocaleExtractor(Optional.empty())),
            Locale.getDefault()),
        new ExtractLocaleCase(Arrays.asList(new StubLocaleExtractor(Optional.of(PROVIDED_LOCALE)),
            new StubLocaleExtractor(Optional.empty())), PROVIDED_LOCALE),
        new ExtractLocaleCase(Arrays.asList(new StubLocaleExtractor(Optional.empty()),
            new StubLocaleExtractor(Optional.of(PROVIDED_LOCALE))), PROVIDED_LOCALE) };
  }

  @RequiredArgsConstructor
  private static class ExtractLocaleCase implements Runnable {
    private final Iterable<PlatformLocaleProvider.LocaleExtractor> localeExtractors;
    private final Locale expectedProvidedLocale;

    @Override
    public void run() {
      // Given
      LocaleProvider localeProvider = new PlatformLocaleProvider(localeExtractors);
      // When
      Locale providedLocale = localeProvider.getLocale();
      // Then
      Assert.assertEquals(expectedProvidedLocale, providedLocale);
    }
  }
}
