package cloud.bangover.functions.registry;

import cloud.bangover.BoundedContextId;
import cloud.bangover.functions.BusinessFunctionRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class is the default implementation of {@link BusinessFunctionRegistry.FunctionMetadata}.
 * 
 * @author Dmitry Mikhaylenko
 */
@RequiredArgsConstructor
class FunctionMetadata implements BusinessFunctionRegistry.FunctionMetadata {
  @Getter
  private final BoundedContextId boundedContextId;
}
