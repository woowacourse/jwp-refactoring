package kitchenpos.repository;

import kitchenpos.domain.TableGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends CrudRepository<TableGroup, Long> {
}
