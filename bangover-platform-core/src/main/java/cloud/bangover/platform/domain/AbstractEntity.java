package cloud.bangover.platform.domain;

import cloud.bangover.BoundedContextId;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction.EntityLifecycleCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractEntity<I> implements Entity<I> {
  @EqualsAndHashCode.Include
  private I id;

  protected abstract BoundedContextId getBoundedContext();

  protected void checkEntityCommandAcceptability(EntityLifecycleCommand<I> command) {
    if (!this.getId().equals(command.getId())) {
      throw new UnacceptableEntityCommandException(getBoundedContext(), this, command);
    }
  }
}
