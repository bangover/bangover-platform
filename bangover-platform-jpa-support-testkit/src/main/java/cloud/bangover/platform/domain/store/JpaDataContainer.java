package cloud.bangover.platform.domain.store;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.transactions.UnitOfWork;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaDataContainer<D> implements DataContainer<D> {
  private final Class<D> entityClass;
  private final EntityManager entityManager;
  private final UnitOfWork unitOfWork;

  @Override
  public DataSet<D> getDataSet() {
    TypedQuery<D> query = entityManager
        .createQuery(String.format("select d from %s d", entityClass.getName()), entityClass);
    return DataSet.of(query.getResultList());
  }

  @Override
  public void loadDataSet(DataSet<D> dataSet) {
    unitOfWork.executeWorkUnit(() -> dataSet.forEach(entityManager::merge));
  }

  @Override
  public void clear() {
    unitOfWork.executeWorkUnit(() -> getDataSet().forEach(entityManager::remove));
  }
}
