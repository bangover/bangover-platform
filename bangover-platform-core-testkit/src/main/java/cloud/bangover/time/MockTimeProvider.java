package cloud.bangover.time;

import java.time.Instant;
import java.util.Optional;

public class MockTimeProvider implements TimeProvider {
    private Optional<Instant> providedInstant;

    public TimeConfigurer configurer() {
        return new TimeConfigurer() {
            @Override
            public void initializeProvidedInstant(Instant providedInstant) {
                MockTimeProvider.this.providedInstant = Optional.ofNullable(providedInstant);
            }

            @Override
            public void reset() {
                MockTimeProvider.this.providedInstant = Optional.empty();
            }
        };
    }

    @Override
    public Instant getCurrentInstant() {
        return providedInstant.orElseGet(Instant::now);
    }

    public interface TimeConfigurer {
        void initializeProvidedInstant(Instant providedInstant);

        void reset();
    }
}