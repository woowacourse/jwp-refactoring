package kitchenpos.persistence;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderTable(OrderTable orderTable);
}
