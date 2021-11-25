package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
