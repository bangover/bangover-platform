package cloud.bangover;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumerationWrapper<E extends Enum<E>> {
  @NonNull
  private final Class<E> enumeration;

  public static final <E extends Enum<E>> EnumerationWrapper<E> of(Class<E> enumeration) {
    return new EnumerationWrapper<E>(enumeration);
  }

  public E findExistingLiteral(Predicate<E> filterFunction) {
    return findLiteral(filterFunction).orElse(null);
  }

  public Optional<E> findLiteral(Predicate<E> filterFunction) {
    return CollectionWrapper.of(Arrays.asList(enumeration.getEnumConstants())).find(filterFunction);
  }

  public boolean containsLiteral(Predicate<E> filterFunction) {
    return findLiteral(filterFunction).isPresent();
  }
}
