package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables, final List<OrderStatus> orderStatuses);
    boolean existsByOrderTableAndOrderStatusIn(final OrderTable orderTable, final List<OrderStatus> orderStatuses);
}
