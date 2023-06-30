package cloud.bangover.async;

import cloud.bangover.async.AsyncContext.LifecycleController;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class AsyncContextLifecyleServletFilter implements Filter {
  private static final Map<ServletRequest, String> contextsBinding =
      new ConcurrentHashMap<ServletRequest, String>();
  private static final LifecycleController lifecycleController =
      new AsyncFlowLifecyclePlatformController();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    startAsyncContext(request);
    initializeServletEnvironment(request, response);
    chain.doFilter(new AsyncRequestWrapper(request), response);
    if (!request.isAsyncStarted()) {
      destroyAsyncContext(request);
    }
  }

  @Override
  public void destroy() {
  }

  private class AsyncRequestWrapper extends HttpServletRequestWrapper {
    public AsyncRequestWrapper(ServletRequest request) {
      super((HttpServletRequest) request);
    }

    @Override
    public javax.servlet.AsyncContext startAsync() throws IllegalStateException {
      javax.servlet.AsyncContext ctx = super.startAsync();
      ctx.addListener(new AsyncLifecycleListener());
      return ctx;
    }

    @Override
    public javax.servlet.AsyncContext startAsync(ServletRequest servletRequest,
        ServletResponse servletResponse) throws IllegalStateException {
      javax.servlet.AsyncContext ctx = super.startAsync(servletRequest, servletResponse);
      ctx.addListener(new AsyncLifecycleListener(), servletRequest, servletResponse);
      return ctx;
    }
  }

  private class AsyncLifecycleListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
      destroyAsyncContext(event.getAsyncContext().getRequest());
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
      destroyAsyncContext(event.getAsyncContext().getRequest());
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
      destroyAsyncContext(event.getAsyncContext().getRequest());
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
      startAsyncContext(event.getAsyncContext().getRequest());
    }
  }

  private static void initializeServletEnvironment(ServletRequest request,
      ServletResponse response) {
    AsyncContext context = Async.getCurrentContext();
    context.attribute(HttpServletRequest.class.getName(), request);
    context.attribute(HttpServletResponse.class.getName(), response);
  }

  private static void startAsyncContext(ServletRequest request) {
    String contextId = lifecycleController.createAsyncContext();
    contextsBinding.putIfAbsent(request, contextId);
  }

  private static void destroyAsyncContext(ServletRequest request) {
    Optional<String> contextId = Optional.ofNullable(contextsBinding.remove(request));
    contextId.ifPresent(lifecycleController::destroyAsyncContext);
  }
}
