package kitchenpos.Order.domain.repository;

import kitchenpos.Order.domain.Order;
import kitchenpos.OrderTable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<String> orderStatus);

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<String> orderStatus);
}
