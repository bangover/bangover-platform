package cloud.bangover.async;

import cloud.bangover.async.AsyncContext.LifecycleController;
import cloud.bangover.events.GlobalEvents;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

public class AsyncFlowContext implements Context {
  private static final String ASYNC_FLOW_SCOPED_BEAN = "cloud.bangover.async.AsyncFlowScoped";
  private final LifecycleController lifecycleController =
      new AsyncFlowLifecyclePlatformController();

  public AsyncFlowContext() {
    super();
    GlobalEvents.subscribeOn(AsyncContextEvent.ASYNC_CONTEXT_CREATED,
        this::initializeContext);
    GlobalEvents.subscribeOn(AsyncContextEvent.ASYNC_CONTEXT_DESTROYED,
        this::destroyContext);
  }

  @Override
  public Class<? extends Annotation> getScope() {
    return AsyncFlowScope.class;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(Contextual<T> contextual) {
    return (T) getBeanInstance((Contextual<Object>) contextual).orElse(null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
    return (T) getOrCreateBeanInsance((Contextual<Object>) contextual,
        (CreationalContext<Object>) creationalContext).get();
  }

  @Override
  public boolean isActive() {
    return true;
  }

  protected void initializeContext(AsyncContextCreated event) {
    getBeansCacheOrRegisterNew().put(event.getContextId(),
        new ConcurrentHashMap<Contextual<Object>, BeanInstance>());
  }

  protected void destroyContext(AsyncContextDestroyed event) {
    removeContextualInsances(event.getContextId()).ifPresent(instances -> {
      instances.forEach((key, value) -> {
        value.destroy();
      });
    });
  }

  private Optional<Map<Contextual<Object>, BeanInstance>> removeContextualInsances(String key) {
    return Optional.of(getBeansCacheOrRegisterNew()).map(cache -> cache.remove(key));
  }
  
  private BeanInstance getOrCreateBeanInsance(Contextual<Object> contextual,
      CreationalContext<Object> creationalContext) {
    return getContextualBeanInstances(contextual).computeIfAbsent(contextual,
        c -> BeanInstance.instanceOf(contextual, creationalContext));
  }

  private Optional<BeanInstance> getBeanInstance(Contextual<Object> contextual) {
    return Optional.ofNullable(getContextualBeanInstances(contextual).get(contextual));
  }

  private Map<Contextual<Object>, BeanInstance> getContextualBeanInstances(
      Contextual<Object> contextual) {
    Map<String, Map<Contextual<Object>, BeanInstance>> cache = getBeansCacheOrRegisterNew();
    return lifecycleController.getCurrentContextId().map(cache::get)
        .orElseGet(ConcurrentHashMap::new);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Map<Contextual<Object>, BeanInstance>> getBeansCacheOrRegisterNew() {
    AsyncContext context = Async.getCurrentContext();
    Map<String, Map<Contextual<Object>, BeanInstance>> result =
        context.attribute(ASYNC_FLOW_SCOPED_BEAN).map(value -> {
          return (Map<String, Map<Contextual<Object>, BeanInstance>>) value;
        }).orElseGet(ConcurrentHashMap::new);
    context.attribute(ASYNC_FLOW_SCOPED_BEAN, result);
    return result;
  }
}
