package cloud.bangover.platform.domain;

import cloud.bangover.platform.domain.values.WrappedValue;

public interface VersionedEntity<I> extends Entity<I> {
  EntityRevision getEntityRevision();

  default EntityRevision incrementEntityVersion() {
    return getEntityRevision().increment();
  }
  
  public static class EntityRevision extends WrappedValue<EntityRevision, Long> {
    private static final long serialVersionUID = -282983886440516653L;

    public EntityRevision() {
      this(null);
    }

    public EntityRevision(Long value) {
      super(value, 0L);
    }

    public EntityRevision increment() {
      return new EntityRevision(getValue() + 1L);
    }
  }
}
