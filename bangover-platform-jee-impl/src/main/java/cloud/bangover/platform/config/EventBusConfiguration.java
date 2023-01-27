package cloud.bangover.platform.config;

import cloud.bangover.actors.ActorName;
import cloud.bangover.actors.ActorSystem;
import cloud.bangover.events.EventBus;
import cloud.bangover.events.PubSubEventBus;
import cloud.bangover.interactions.pubsub.ActorSystemPubSub;
import cloud.bangover.interactions.pubsub.PubSub;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class EventBusConfiguration {
  private static final ActorName EVENTS_CHANNEL_ACTOR_NAME =
      ActorName.wrap(String.format("GLOBAL-EVENTS--%s", UUID.randomUUID()));

  @Inject
  private ActorSystem actorSystem;

  @Produces
  @ApplicationScoped
  public EventBus.Factory eventsBusFactory() {
    PubSub<Object> channel =
        ActorSystemPubSub.factory(actorSystem).createPubSub(EVENTS_CHANNEL_ACTOR_NAME);
    return PubSubEventBus.factory(channel);
  }
}
