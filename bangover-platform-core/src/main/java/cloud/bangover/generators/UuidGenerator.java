package cloud.bangover.generators;

import java.util.UUID;

public class UuidGenerator implements Generator<UUID> {
  @Override
  public UUID generateNext() {
    return UUID.randomUUID();
  }

  public Generator<String> generateStrings() {
    return () -> UuidGenerator.this.generateNext().toString();
  }
}
