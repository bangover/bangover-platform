package cloud.bangover.async;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanInstance {
  private final Object instance;
  private final Bean<Object> bean;
  private final CreationalContext<Object> creationalContext;

  public static BeanInstance instanceOf(Contextual<Object> contextual,
      CreationalContext<Object> creationalContext) {
    return new BeanInstance(contextual.create(creationalContext), (Bean<Object>) contextual,
        creationalContext);
  }
  
  public Object get() {
    return this.instance;
  }

  public void destroy() {
    this.bean.destroy(instance, creationalContext);
  }
}
