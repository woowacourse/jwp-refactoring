package kitchenpos.repositroy;

import kitchenpos.domain.table_group.TableGroup;
import kitchenpos.exception.TableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long id) {
        return findById(id).orElseThrow(() -> new TableGroupException.NotFoundException(id));
    }
}
