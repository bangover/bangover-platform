package cloud.bangover.platform.domain;

import cloud.bangover.events.EventType;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.events.LocalEventBus;
import cloud.bangover.events.MockEventListener;
import cloud.bangover.platform.domain.functions.EntityNotFoundException;
import cloud.bangover.platform.domain.store.MockEntityStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EntityEventTest {
  private static final Long ENTITY_ID = 1L;
  private static final StubEntity ENTITY = new StubEntity(ENTITY_ID);
  private static final StubEntityEvent EVENT = new StubEntityEvent(ENTITY_ID);

  @Test
  public void shouldEntityBeRetrieved() {
    // Given
    MockStore store = new MockStore();
    GlobalEvents.configureEventBus(new LocalEventBus());
    MockEventListener<StubEntityEvent> eventListener = new MockEventListener<>();
    GlobalEvents.subscribeOn(StubEntityEvent.EVENT_TYPE, eventListener);
    store.save(ENTITY);
    
    // When
    GlobalEvents.publisher(StubEntityEvent.EVENT_TYPE).publish(EVENT);
    
    
    // Then
    StubEntityEvent publishedEvent = eventListener.getHistory().getEntry(0);    
    Assert.assertSame(EVENT, publishedEvent);
    Assert.assertSame(ENTITY, publishedEvent.getEntity(store));
    
    // Cleanup
    GlobalEvents.reset();
    store.clear();
  }
  
  @Test
  public void shouldNotEntityBeRetrievedIfItIsNotExistInTheStore() {
    // Given
    MockStore store = new MockStore();
    GlobalEvents.configureEventBus(new LocalEventBus());
    MockEventListener<StubEntityEvent> eventListener = new MockEventListener<>();
    GlobalEvents.subscribeOn(StubEntityEvent.EVENT_TYPE, eventListener);
    
    // When
    GlobalEvents.publisher(StubEntityEvent.EVENT_TYPE).publish(EVENT);
    
    // Then
    StubEntityEvent publishedEvent = eventListener.getHistory().getEntry(0);    
    Assert.assertSame(EVENT, publishedEvent);
    Assert.assertThrows(EntityNotFoundException.class, () -> {
      publishedEvent.getEntity(store);
    });
    
    // Cleanup
    GlobalEvents.reset();
    store.clear();
  }


  private static class StubEntityEvent extends EntityEvent<Long, StubEntity, StubEntityEvent> {
    public static EventType<StubEntityEvent> EVENT_TYPE =
        EventType.createFor("STUB_EVENT", StubEntityEvent.class);

    public StubEntityEvent(Long entityId) {
      super(EVENT_TYPE, entityId);
    }
  }

  private static class MockStore extends MockEntityStore<Long, StubEntity> {
    public MockStore() {
      super();
    }
  }

  private static class StubEntity extends AbstractEntity<Long> {
    public StubEntity(Long id) {
      super(id);
    }
  }
}
