package kitchenpos.table_group.domain;

import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.TABLE_GROUP_NOT_FOUND;

import kitchenpos.table_group.domain.exception.TableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long tableGroupId) {
        return findById(tableGroupId)
            .orElseThrow(() -> new TableGroupException(TABLE_GROUP_NOT_FOUND));
    }
}
