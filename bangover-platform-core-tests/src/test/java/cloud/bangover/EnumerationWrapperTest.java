package cloud.bangover;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

@RunWith(JUnit4.class)
public class EnumerationWrapperTest {
  @Test
  public void shouldFindExistingLiteralReturnEnumItemIfExists() {
    Assert.assertEquals(Items.ITEM, EnumerationWrapper.of(Items.class)
        .findExistingLiteral(value -> "ITEM".equals(value.name())));
  }

  @Test
  public void shouldFindExistingLiteralReturnNullItemIfDoesNotExist() {
    Assert.assertNull(EnumerationWrapper.of(Items.class)
        .findExistingLiteral(value -> "UNKNOWN".equals(value.name())));
  }

  @Test
  public void shouldFindLiteralReturnEnumItemIfExists() {
    Assert.assertEquals(Optional.of(Items.ITEM),
        EnumerationWrapper.of(Items.class).findLiteral(value -> "ITEM".equals(value.name())));
  }

  @Test
  public void shouldFindLiteralReturnEmptyItemIfDoesNotExist() {
    Assert.assertFalse(EnumerationWrapper.of(Items.class)
        .findLiteral(value -> "UNKNOWN".equals(value.name())).isPresent());
  }

  @Test
  public void shouldContainsLiteralReturnTrueIfMatchedLiteralIsContained() {
    Assert.assertTrue(
        EnumerationWrapper.of(Items.class).containsLiteral(value -> "ITEM".equals(value.name())));
  }

  @Test
  public void shouldContainsLiteralReturnFalseIfMatchedLiteralIsNotContained() {
    Assert.assertFalse(EnumerationWrapper.of(Items.class)
        .containsLiteral(value -> "UNKNOWN".equals(value.name())));
  }

  private enum Items {
    ITEM;
  }
}
