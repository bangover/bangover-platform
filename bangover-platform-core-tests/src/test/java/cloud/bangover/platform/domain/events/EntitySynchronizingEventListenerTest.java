package cloud.bangover.platform.domain.events;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.events.EventType;
import cloud.bangover.events.LocalEventBus;
import cloud.bangover.platform.domain.AbstractEntity;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EntitySynchronizingEventListenerTest {
  @Test
  public void shouldEntityBeSynchronized() {
    // Given
    StubProvider entityProvider = new StubProvider();
    MockStore entityStore = new MockStore();
    StubEvent event = new StubEvent();
    EventBus eventBus = new LocalEventBus();
    EventSubscribtion subscription = eventBus.subscribeOn(StubEvent.EVENT_TYPE,
        new MockSynchronizer(entityProvider, entityStore));
    entityProvider.configure(configurer -> {
      configurer.withProvidedEntity(event, new MockEntity(1L));
    });

    // When
    eventBus.getPublisher(StubEvent.EVENT_TYPE).publish(event);

    // Then
    List<MockEntity> storedEntities = entityStore.getDataSet().toList();
    Assert.assertEquals(1, storedEntities.size());
    MockEntity entity = storedEntities.get(0);
    Assert.assertEquals(1L, (long) entity.getId());
    Assert.assertTrue(entity.isSynchronized());

    // Cleanup
    subscription.unsubscribe();
    entityProvider.clear();
    entityStore.clear();
  }

  public static class MockSynchronizer
      extends EntitySynchronizingEventListener<Long, MockEntity, StubEvent> {

    private MockSynchronizer(StubProvider entitiesProvider, MockStore entityStore) {
      super(entitiesProvider, entityStore, (entity, event) -> event.synchronize(entity));
    }
  }

  private static class StubProvider
      extends StubSynchronizingEntitiesProvider<Long, MockEntity, StubEvent> {
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

  private static class StubEvent {
    public static EventType<StubEvent> EVENT_TYPE =
        EventType.createFor("MOCK_EVENT", StubEvent.class);

    public void synchronize(MockEntity entity) {
      entity.synchronize();
    }
  }
}
