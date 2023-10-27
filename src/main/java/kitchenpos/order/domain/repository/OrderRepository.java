package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableAndOrderStatusIn(final OrderTable orderTable,
                                               final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables,
                                                 final List<OrderStatus> orderStatuses);
}
