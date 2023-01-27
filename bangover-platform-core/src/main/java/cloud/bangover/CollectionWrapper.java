package cloud.bangover;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionWrapper<T> {
  @ToString.Include
  @EqualsAndHashCode.Include
  private final Collection<T> collection;

  public static <T> CollectionWrapper<T> of(Collection<T> collection) {
    return new CollectionWrapper<>(Optional.ofNullable(collection).orElse(Collections.emptyList()));
  }

  public Stream<T> stream() {
    return collection.stream();
  }

  public boolean hasMatched(Predicate<T> matcher) {
    return stream().anyMatch(matcher);
  }

  public boolean containsAll(Collection<T> values) {
    return collection.containsAll(values);
  }

  public boolean containsOneOf(Collection<T> values) {
    return hasMatched(value -> values.contains(value));
  }

  public Optional<T> firstElement() {
    return stream().findFirst();
  }

  public Long count() {
    return stream().count();
  }

  public Optional<T> find(Predicate<T> filter) {
    return filteredStream(filter).findFirst();
  }

  public CollectionWrapper<T> skip(Long offset) {
    return new CollectionWrapper<>(skippedStream(offset).collect(Collectors.toList()));
  }

  public CollectionWrapper<T> limit(Long limit) {
    return new CollectionWrapper<>(limitedStream(limit).collect(Collectors.toList()));
  }

  public CollectionWrapper<T> filter(Predicate<T> filter) {
    return new CollectionWrapper<>(filteredStream(filter).collect(Collectors.toList()));
  }

  public CollectionWrapper<T> sort(Comparator<T> comparator) {
    return new CollectionWrapper<>(orderedStream(comparator).collect(Collectors.toList()));
  }

  public void forEach(Consumer<? super T> iterationHandler) {
    stream().forEach(iterationHandler);
  }

  public <K> Map<K, T> classify(Function<T, K> classifier) {
    return asMap(classifier, value -> value);
  }

  public <K, V> Map<K, V> asMap(Function<T, K> classifier, Function<T, V> mapper) {
    return stream().collect(Collectors.toMap(classifier, mapper));
  }

  public <K, C extends Collection<T>> Map<K, C> group(Function<T, K> classifier,
      Supplier<C> collectionCreator) {
    return stream()
        .collect(Collectors.groupingBy(classifier, Collectors.toCollection(collectionCreator)));
  }

  public CollectionWrapper<T> distinct() {
    return new CollectionWrapper<>(stream().distinct().collect(Collectors.toList()));
  }

  public <Q> CollectionWrapper<Q> map(Function<T, Q> converter) {
    return new CollectionWrapper<>(stream().map(converter).collect(Collectors.toList()));
  }

  public <Q> CollectionWrapper<Q> flatMap(Function<T, Collection<Q>> converter) {
    return new CollectionWrapper<>(
        stream().flatMap(item -> converter.apply(item).stream()).collect(Collectors.toList()));
  }

  public <C extends Collection<T>> C normalize(Supplier<C> collectionFactory) {
    return stream().collect(Collectors.toCollection(collectionFactory));
  }

  public <C> CollectionWrapper<C> cast(Class<C> castType) {
    return new CollectionWrapper<>(stream().map(castType::cast).collect(Collectors.toList()));
  }

  public Collection<T> get() {
    return collection;
  }

  private Stream<T> skippedStream(Long offset) {
    return stream().skip(offset);
  }

  private Stream<T> limitedStream(Long limit) {
    return stream().limit(limit);
  }

  private Stream<T> filteredStream(Predicate<T> filter) {
    return stream().filter(filter);
  }

  private Stream<T> orderedStream(Comparator<T> comparator) {
    return stream().sorted(comparator);
  }
}
