package cloud.bangover.platform.config;

import cloud.bangover.locale.LocaleProvider;
import cloud.bangover.text.PlatformLocaleProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class LocaleProviderConfiguration {
  @Inject
  private Instance<PlatformLocaleProvider.LocaleExtractor> localeExtractors;

  @Produces
  @ApplicationScoped
  public LocaleProvider localeProvider() {
    return new PlatformLocaleProvider(localeExtractors);
  }
}
