package kitchenpos.dao.order;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables, final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableAndOrderStatusIn(final OrderTable orderTable, final List<OrderStatus> orderStatuses);
}
