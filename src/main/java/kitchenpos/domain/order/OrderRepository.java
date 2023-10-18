package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderTable(OrderTable orderTable);
}
