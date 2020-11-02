package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByTable_IdAndOrderStatusIn(Long tableId, List<OrderStatus> orderStatuses);

    boolean existsByTableInAndOrderStatusIn(List<Table> tables, List<OrderStatus> orderStatuses);
}
