package cloud.bangover.async;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ApplicationScoped
public class AsyncContextualServletConfig {
  @Produces
  @AsyncFlowScope
  @AsyncContextual
  public HttpServletRequest asyncScopedHttpServletRequest() {
    AsyncContext context = Async.getCurrentContext();
    return context.attribute(HttpServletRequest.class.getName())
        .map(HttpServletRequest.class::cast).orElse(null);
  }

  @Produces
  @AsyncFlowScope
  @AsyncContextual
  public HttpServletResponse asyncScopedHttpServletResponse() {
    AsyncContext context = Async.getCurrentContext();
    return context.attribute(HttpServletResponse.class.getName())
        .map(HttpServletResponse.class::cast).orElse(null);
  }
}
