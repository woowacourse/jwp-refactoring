package kitchenpos.domain.table;

import static kitchenpos.exception.table.TableGroupExceptionType.TABLE_GROUP_NOT_FOUND;

import kitchenpos.exception.table.TableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Override
    default TableGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new TableGroupException(TABLE_GROUP_NOT_FOUND));
    }
}
