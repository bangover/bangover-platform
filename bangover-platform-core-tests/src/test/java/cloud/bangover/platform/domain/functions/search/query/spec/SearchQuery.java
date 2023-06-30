package cloud.bangover.platform.domain.functions.search.query.spec;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class SearchQuery {
  private String discriminator;
}
