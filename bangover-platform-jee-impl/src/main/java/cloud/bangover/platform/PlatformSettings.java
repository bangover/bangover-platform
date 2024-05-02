package cloud.bangover.platform;

import cloud.bangover.actors.CorrelationKeyGenerator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface PlatformSettings {
  default CorrelationKeyGenerator correlationKeyGenerator() {
    return new CorrelationKeyGenerator("CKEY");
  }
  
  default ExecutorService executorService() {
    return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }
}
