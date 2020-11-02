package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByTable_IdAndOrderStatusIn(Long tableId, List<String> orderStatuses);

    boolean existsByTableInAndOrderStatusIn(List<Table> tables, List<String> orderStatuses);
}
