package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTable(OrderTable orderTable);

    List<Order> findAllByOrderTableIn(List<OrderTable> orderTables);
}
