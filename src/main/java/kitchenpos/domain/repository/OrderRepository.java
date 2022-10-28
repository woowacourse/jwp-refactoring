package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<String> orderStatuses);

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<String> orderStatuses);
}
