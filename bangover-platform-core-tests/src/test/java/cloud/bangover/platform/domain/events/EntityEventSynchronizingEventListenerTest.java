package cloud.bangover.platform.domain.events;

import cloud.bangover.events.EventType;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.events.LocalEventBus;
import cloud.bangover.platform.domain.AbstractEntity;
import cloud.bangover.platform.domain.EntityEvent;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EntityEventSynchronizingEventListenerTest {
  private static final Long ENTITY_ID = 1L;

  @Test
  public void shouldEntityBeSynchronized() {
    // Given
    MockStore entityStore = new MockStore();
    StubEvent event = new StubEvent(ENTITY_ID);
    GlobalEvents.configureEventBus(new LocalEventBus());
    entityStore.save(new MockEntity(ENTITY_ID));
    MockSynchronizer mockSynchronizer = new MockSynchronizer(entityStore);
    GlobalEvents.subscribeOn(StubEvent.EVENT_TYPE, mockSynchronizer);

    // When
    GlobalEvents.publisher(StubEvent.EVENT_TYPE).publish(event);

    // Then
    List<MockEntity> storedEntities = entityStore.getDataSet().toList();
    
    Assert.assertEquals(1, storedEntities.size());
    MockEntity entity = storedEntities.get(0);
    Assert.assertEquals(1L, (long) entity.getId());
    Assert.assertTrue(entity.isSynchronized());
    
    // Cleanup
    GlobalEvents.reset();
    entityStore.clear();
  }

  public static class MockSynchronizer
      extends EntityEventSynchronizingEventListener<Long, MockEntity, StubEvent> {

    private MockSynchronizer(MockStore entityStore) {
      super(entityStore, (entity, event) -> event.synchronize(entity));
    }
  }

  private static class MockStore extends MockEntityStore<Long, MockEntity> {
  }

  @RequiredArgsConstructor
  private static class MockEntity extends AbstractEntity<Long> {
    private boolean sync = false;

    public MockEntity(Long id) {
      super(id);
    }

    public boolean isSynchronized() {
      return sync;
    }

    public void synchronize() {
      this.sync = true;
    }
  }

  private static class StubEvent extends EntityEvent<Long, MockEntity, StubEvent> {
    public static EventType<StubEvent> EVENT_TYPE =
        EventType.createFor("MOCK_EVENT", StubEvent.class);

    public StubEvent(Long entityId) {
      super(EVENT_TYPE, entityId);
    }

    public void synchronize(MockEntity entity) {
      entity.synchronize();
    }
  }
}
