package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByOrderTable(OrderTable orderTable);

    List<Orders> findAllByOrderTableIn(List<OrderTable> orderTables);
}
