package cloud.bangover.platform.domain.store;

public interface CountSpecification<C> {
  Long count(C context);
}
