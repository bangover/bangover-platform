package cloud.bangover.platform.domain.functions.search.query;

import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Pagination {
  private static final Integer MAX_LIMIT = 5000;
  private static final Integer DEFAULT_LIMIT = 100;
  private static final Long DEFAULT_PAGE = 1L;

  private Long page;
  private Integer size;

  public Pagination() {
    this(Optional.empty(), Optional.empty());
  }

  public Pagination(@NonNull Optional<Long> page, @NonNull Optional<Integer> size) {
    super();
    this.page = normalizePage(page);
    this.size = normalizeSize(size);
  }

  private static Long normalizePage(Optional<Long> page) {
    return page.filter(value -> value.compareTo(0L) > 0).orElse(DEFAULT_PAGE);
  }

  private static Integer normalizeSize(Optional<Integer> limit) {
    return limit.filter(value -> value.compareTo(0) > 0)
        .filter(value -> value.compareTo(MAX_LIMIT) <= 0).orElse(DEFAULT_LIMIT);
  }

  public Long getOffset() {
    return (getPage() - 1) * getSize();
  }
}
