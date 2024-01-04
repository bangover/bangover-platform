package cloud.bangover.platform.domain.events;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.events.EventType;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.events.LocalEventBus;
import cloud.bangover.events.MockEventListener;
import cloud.bangover.platform.domain.AbstractEventDrivenEntity;
import cloud.bangover.platform.domain.EntityEvent;
import cloud.bangover.platform.domain.EntityEventsManager;
import cloud.bangover.platform.domain.EventDrivenEntity;
import cloud.bangover.platform.domain.VersionedEntity;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.List;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

public class EventDrivenEntitySynchronizingEventListenerTest {

  @Test
  public void shouldEventDrivenEntityBeSynchronized() {
    // Given
    StubProvider entityProvider = new StubProvider();
    MockStore entityStore = new MockStore();
    StubEvent event = new StubEvent();
    EventBus eventBus = new LocalEventBus();
    EventSubscribtion subscription = eventBus.subscribeOn(StubEvent.EVENT_TYPE,
        new MockSynchronizer(entityProvider, entityStore));
    MockEventListener<StubEntityEvent> globalEntityListener = new MockEventListener<>();
    GlobalEvents.configureEventBus(eventBus);
    GlobalEvents.subscribeOn(StubEntityEvent.EVENT_TYPE, globalEntityListener);
    entityProvider.configure(configurer -> {
      configurer.withProvidedEntity(event, new MockEventDrivenEntity(1L));
    });

    // When
    eventBus.getPublisher(StubEvent.EVENT_TYPE).publish(event);

    // Then
    List<MockEventDrivenEntity> storedEntities = entityStore.getDataSet().toList();
    Assert.assertEquals(1, storedEntities.size());
    MockEventDrivenEntity entity = storedEntities.get(0);
    Assert.assertEquals(1L, (long) entity.getId());
    Assert.assertEquals(1, globalEntityListener.getHistory().getLength());
    Assert.assertEquals(1L, (long) entity.getEntityRevision().getValue());
    StubEntityEvent entityEvent = globalEntityListener.getHistory().getEntry(0);
    Assert.assertEquals(StubEntityEvent.EVENT_TYPE, entityEvent.getEventType());
    Assert.assertEquals(entity, entityEvent.getEntity());

    // Cleanup
    GlobalEvents.reset();
    subscription.unsubscribe();
    entityProvider.clear();
    entityStore.clear();
  }

  private static class MockSynchronizer
      extends EventDrivenEntitySynchronizingEventListener<Long, MockEventDrivenEntity, StubEvent> {
    public MockSynchronizer(StubProvider synchronizingEntitiesProvider, MockStore entityStore) {
      super(synchronizingEntitiesProvider, entityStore,
          (entity, event) -> event.synchronize(entity));
    }
  }

  private static class StubProvider
      extends StubSynchronizingEntitiesProvider<Long, MockEventDrivenEntity, StubEvent> {

  }

  private static class MockStore extends MockEntityStore<Long, MockEventDrivenEntity> {
  }

  private static class MockEventDrivenEntity
      extends AbstractEventDrivenEntity<Long, MockEventDrivenEntity>
      implements EventDrivenEntity<Long, MockEventDrivenEntity>, VersionedEntity<Long> {
    @Getter
    private EntityRevision entityRevision = new EntityRevision();

    @Getter
    private final EntityEventsManager<Long, MockEventDrivenEntity> entityEventsManager =
        new EntityEventsManager<>();

    public MockEventDrivenEntity(Long id) {
      super(id);
    }

    public void synchronize() {
      this.entityRevision = incrementEntityVersion();
      publishDomainEvent(new StubEntityEvent(this));
    }

  }

  private static class StubEvent {
    public static EventType<StubEvent> EVENT_TYPE =
        EventType.createFor("MOCK_EVENT", StubEvent.class);

    public void synchronize(MockEventDrivenEntity entity) {
      entity.synchronize();
    }
  }

  private static class StubEntityEvent
      extends EntityEvent<Long, MockEventDrivenEntity, StubEntityEvent> {
    public static EventType<StubEntityEvent> EVENT_TYPE =
        EventType.createFor("ENTITY_EVENT", StubEntityEvent.class);

    public StubEntityEvent(MockEventDrivenEntity entity) {
      super(EVENT_TYPE, entity);
    }
  }

}
