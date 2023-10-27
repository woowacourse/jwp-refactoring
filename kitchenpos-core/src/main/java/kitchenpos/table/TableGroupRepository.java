package kitchenpos.table;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    boolean existsByOrderTablesIn(final List<OrderTable> orderTables);
}
