package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderTable(OrderTable orderTable);

    List<Order> findByOrderTableIn(List<OrderTable> orderTables);
}
