package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.text.Text;
import cloud.bangover.text.TextProcessor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TextProcessingConfigurer {
  @Inject
  private TextProcessor textProcessor;

  public void configureTextProcessing() {
    Text.configureProcessor(textProcessor);
  }

  public void destroyTextProcessing() {
    Text.reset();
  }
}
