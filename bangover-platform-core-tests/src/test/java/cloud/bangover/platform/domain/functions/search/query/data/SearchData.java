package cloud.bangover.platform.domain.functions.search.query.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(staticName = "of")
public class SearchData {
  @EqualsAndHashCode.Include
  private final Integer id;
  private final String discriminator;
}
