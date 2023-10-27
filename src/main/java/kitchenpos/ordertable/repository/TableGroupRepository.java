package kitchenpos.ordertable.repository;

import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    default TableGroup getById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
