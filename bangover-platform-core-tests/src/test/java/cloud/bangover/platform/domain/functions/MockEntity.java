package cloud.bangover.platform.domain.functions;

import cloud.bangover.generators.Generator;
import cloud.bangover.platform.domain.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MockEntity implements Entity<Long> {
  @EqualsAndHashCode.Include
  private Long id;
  private String description = "N/A";

  public MockEntity(Generator<Long> generator) {
    this(generator.generateNext());
  }

  public MockEntity(Long id) {
    super();
    this.id = id;
  }

  public MockEntity(Generator<Long> generator, String description) {
    this(generator.generateNext(), description);
  }

  public MockEntity(Long id, String description) {
    super();
    this.id = id;
    this.description = description;
  }

  public void edit(String description) {
    this.description = description;
  }
}
