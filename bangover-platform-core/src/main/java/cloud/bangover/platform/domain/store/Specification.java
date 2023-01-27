package cloud.bangover.platform.domain.store;

public interface Specification<C> {
  void applyTo(C context);

  interface Factory<C> {
    Specification<C> createSpecification();
  }

  interface ParameterizedFactory<C, Q> {
    Specification<C> createSpecification(Q requestData);
  }
}