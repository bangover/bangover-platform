package cloud.bangover.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class BusinessFunctionDecoratorsCascade implements BusinessFunctionDecorator {
  private final BusinessFunctionDecorator original;
  private final BusinessFunctionDecorator chained;

  @Override
  public <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> decorated) {
    return original.decorate(chained.decorate(decorated));
  }
}
