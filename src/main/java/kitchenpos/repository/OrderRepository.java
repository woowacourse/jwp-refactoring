package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses);
}
