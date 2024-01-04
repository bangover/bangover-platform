package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.time.Time.TimeModuleRegistrar;
import cloud.bangover.time.TimeProvider;
import cloud.bangover.time.ZoneProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TimeModuleLifecycleConfigurer {
  @Inject
  private TimeProvider timeProvider;

  @Inject
  private ZoneProvider zoneProvider;

  public void configureTimeModule(TimeModuleRegistrar registrar) {
    registrar.registerTimeProvider(timeProvider).registerZoneProvider(zoneProvider);
  }
}
