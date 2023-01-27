package cloud.bangover.time;

import java.time.ZoneOffset;
import java.util.Optional;

public class MockZoneProvider implements ZoneProvider {
    private Optional<ZoneOffset> zoneOffset;

    public ZoneConfigurer configurer() {
        return new ZoneConfigurer() {
            @Override
            public void initializeProvidedZoneOffset(ZoneOffset zoneOffset) {
                MockZoneProvider.this.zoneOffset = Optional.ofNullable(zoneOffset);
            }

            @Override
            public void reset() {
                MockZoneProvider.this.zoneOffset = Optional.empty();
            }
        };
    }

    @Override
    public ZoneOffset getZoneOffset() {
        return zoneOffset.orElse(ZoneOffset.UTC);
    }

    public interface ZoneConfigurer {
        void initializeProvidedZoneOffset(ZoneOffset zoneOffset);

        void reset();
    }
}
