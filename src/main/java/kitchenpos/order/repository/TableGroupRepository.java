package kitchenpos.order.repository;

import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.exception.TableGroupException.NotExistsTableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long tableGroupId) {
        return findById(tableGroupId)
                .orElseThrow(() -> new NotExistsTableGroupException(tableGroupId));
    }
}
