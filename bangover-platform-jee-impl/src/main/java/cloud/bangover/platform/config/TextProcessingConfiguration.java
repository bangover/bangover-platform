package cloud.bangover.platform.config;

import cloud.bangover.locale.LocaleProvider;
import cloud.bangover.text.TextProcessor;
import cloud.bangover.text.TextTemplate;
import cloud.bangover.text.TextTransformers;
import cloud.bangover.text.compilers.HandlebarsTemplateCompiler;
import cloud.bangover.text.resolvers.ResourceBundleResolver;
import cloud.bangover.text.transformers.BundleResolvingTransformer;
import cloud.bangover.text.transformers.TemplateCompilingTransformer;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class TextProcessingConfiguration {
  @Inject
  private LocaleProvider localeProvider;

  @Produces
  public TextProcessor textProcessor() {
    return TextProcessor.create().withTransformer(createTextTransformersChain());
  }

  private TextTemplate.Transformer createTextTransformersChain() {
    BundleResolvingTransformer.BundleResolver bundleResolver =
        new ResourceBundleResolver(localeProvider);
    TemplateCompilingTransformer.TemplateCompiler templateCompiler =
        new HandlebarsTemplateCompiler();
    TextTemplate.Transformer result = TextTransformers.trimming();
    result = TextTransformers.chain(result, new BundleResolvingTransformer(bundleResolver));
    result = TextTransformers.chain(result, new TemplateCompilingTransformer(templateCompiler));
    return TextTransformers.deepDive(result);
  }
}
