package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableId(Long orderTableId);

    List<Order> findAllByOrderTableIn(List<OrderTable> orderTables);
}
