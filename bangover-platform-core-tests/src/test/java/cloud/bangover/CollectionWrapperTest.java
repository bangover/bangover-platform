package cloud.bangover;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

@RunWith(JUnit4.class)
public class CollectionWrapperTest {
  @Test
  public void shouldHasMatchedReturnTrueIfCollectionContainsElement() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    boolean result = collectionWrapper.hasMatched(value -> value == 2);
    // Then
    Assert.assertTrue(result);
  }

  @Test
  public void shouldHasMatchedReturnFalseIfCollectionDoesNotContainElement() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    boolean result = collectionWrapper.hasMatched(value -> value == 10);
    // Then
    Assert.assertFalse(result);
  }

  @Test
  public void shouldContainsAllReturnTrueIfCollectionContainsAll() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    boolean result = collectionWrapper.containsAll(Arrays.asList(1, 3, 4));
    // Then
    Assert.assertTrue(result);
  }

  @Test
  public void shouldContainsAllReturnFalseIfCollectionDoesNotContainAtLeastOne() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    boolean result = collectionWrapper.containsAll(Arrays.asList(1, 3, 4, 5));
    // Then
    Assert.assertFalse(result);
  }

  @Test
  public void shouldContainsOneOfReturnTrueIfCollectionContainsAtLeastOne() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    boolean result = collectionWrapper.containsOneOf(Arrays.asList(1, 5, 6, 7));
    // Then
    Assert.assertTrue(result);
  }

  @Test
  public void shouldContainsOneOfReturnFalseIfCollectionDoesNotContainAtLeastOne() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    boolean result = collectionWrapper.containsOneOf(Arrays.asList(5, 6, 7));
    // Then
    Assert.assertFalse(result);
  }

  @Test
  public void shouldFirstElementReturnFirstCollectionElementIfCollectionNotEmpty() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Optional<Integer> result = collectionWrapper.firstElement();
    // Then
    Assert.assertEquals(Optional.of(1), result);
  }

  @Test
  public void shouldFirstElementReturnEmptyElementIfCollectionNotEmpty() {
    // Given
    Collection<Integer> items = Collections.emptyList();
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Optional<Integer> result = collectionWrapper.firstElement();
    // Then
    Assert.assertEquals(Optional.empty(), result);
  }

  @Test
  public void shouldCountReturnCollectionSize() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Long result = collectionWrapper.count();
    // Then
    Assert.assertEquals((Long) 4L, result);
  }

  @Test
  public void shouldFindReturnFoundElementIfItIsMatchedToThePredicate() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Optional<Integer> result = collectionWrapper.find(value -> value == 4);
    // Then
    Assert.assertEquals((Integer) 4, result.get());
  }

  @Test
  public void shouldFindReturnEmptyElementIfItIsNotMatchedToThePredicate() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Optional<Integer> result = collectionWrapper.find(value -> value == 6);
    // Then
    Assert.assertFalse(result.isPresent());
  }

  @Test
  public void shouldSkipReturnDerivedCollectionWithoutFirstSkippedElements() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.skip(2L);
    // Then
    Assert.assertEquals(Arrays.asList(3, 4), new ArrayList<>(collectionWrapper.get()));
  }

  @Test
  public void shouldSkipReturnEmptyCollectionForEmptyCollection() {
    // Given
    Collection<Integer> items = Collections.emptyList();
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.skip(2L);
    // Then
    Assert.assertTrue(collectionWrapper.get().isEmpty());
  }

  @Test
  public void shouldLimitReturnTruncatedCollectionWithoutLastElements() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.limit(2L);
    // Then
    Assert.assertEquals(Arrays.asList(1, 2), new ArrayList<>(collectionWrapper.get()));
  }

  @Test
  public void shouldLimitReturnEmptyCollectionForEmptyCollection() {
    // Given
    Collection<Integer> items = Collections.emptyList();
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.limit(2L);
    // Then
    Assert.assertTrue(collectionWrapper.get().isEmpty());
  }

  @Test
  public void shouldFilterReturnMatchedToPredicateItems() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.filter(value -> value % 2 == 0);
    // Then
    Assert.assertEquals(Arrays.asList(2, 4), new ArrayList<>(collectionWrapper.get()));
  }

  @Test
  public void shouldSortReturnOrderedCollection() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.sort(Comparator.reverseOrder());
    // Then
    Assert.assertEquals(Arrays.asList(4, 3, 2, 1), new ArrayList<>(collectionWrapper.get()));
  }

  @Test
  public void shouldForEachIterateByAllCollection() {
    // Given
    List<Integer> result = new ArrayList<>();
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper.forEach(result::add);
    // Then
    Assert.assertEquals(Arrays.asList(1, 2, 3, 4), result);
  }

  @Test
  public void shouldClassifyReturnMapOfElements() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Map<String, Integer> result = collectionWrapper.classify(value -> value.toString());
    // Then
    Assert.assertEquals((Integer) 1, result.get("1"));
    Assert.assertEquals((Integer) 2, result.get("2"));
    Assert.assertEquals((Integer) 3, result.get("3"));
    Assert.assertEquals((Integer) 4, result.get("4"));
  }

  @Test
  public void shouldGroupReturnMapOfElementsGroupedBySpecialAttribute() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Map<String, List<Integer>> result = collectionWrapper.group(value -> {
      if (value % 2 == 0) {
        return "even";
      }
      return "odd";
    }, ArrayList::new);
    // Then
    Assert.assertEquals(Arrays.asList(1, 3), result.get("odd"));
    Assert.assertEquals(Arrays.asList(2, 4), result.get("even"));
  }

  @Test
  public void shouldDistinctReturnItemsWithoutRepetitions() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 1, 2, 2, 3, 3, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    collectionWrapper = collectionWrapper.distinct();
    // Then
    Assert.assertEquals(Arrays.asList(1, 2, 3, 4), new ArrayList<>(collectionWrapper.get()));
  }

  @Test
  public void shouldMapReturnConvertedItems() {
    // Given
    Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    CollectionWrapper<String> result = collectionWrapper.map(value -> value.toString());
    // Then
    Assert.assertEquals(Arrays.asList("1", "2", "3", "4"), new ArrayList<>(result.get()));
  }

  @Test
  public void shouldMapReturnConvertedItemsWithNestedCollectionsUnwrapping() {
    // Given
    Collection<Collection<Integer>> items = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4));
    CollectionWrapper<Collection<Integer>> collectionWrapper = CollectionWrapper.of(items);
    // When
    CollectionWrapper<String> result = collectionWrapper
        .flatMap(values -> CollectionWrapper.of(values).map(value -> value.toString()).get());
    // Then
    Assert.assertEquals(Arrays.asList("1", "2", "3", "4"), new ArrayList<>(result.get()));
  }

  @Test
  public void shouldNormalizeReturnCollectionWithSpecifiedType() {
    // Given
    Collection<Integer> items = Arrays.asList(4, 3, 2, 1);
    CollectionWrapper<Integer> collectionWrapper = CollectionWrapper.of(items);
    // When
    Set<Integer> result = collectionWrapper.normalize(TreeSet::new);
    // Then
    Assert.assertEquals(new TreeSet<>(items), result);
  }

  @Test
  public void shouldCastReturnItemsCastedToSpecifiedType() {
    // Given
    Collection<Object> items = Arrays.asList(1, 2, 3, 4);
    CollectionWrapper<Object> collectionWrapper = CollectionWrapper.of(items);
    // When
    CollectionWrapper<Integer> castedWrapper = collectionWrapper.cast(Integer.class);
    // Then
    Assert.assertEquals(collectionWrapper, castedWrapper);
  }
}
