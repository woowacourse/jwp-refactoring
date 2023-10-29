package kitchenpos.table.domain;

import static kitchenpos.table.exception.TableGroupExceptionType.TABLE_GROUP_NOT_FOUND;

import kitchenpos.table.exception.TableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Override
    default TableGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new TableGroupException(TABLE_GROUP_NOT_FOUND));
    }
}
