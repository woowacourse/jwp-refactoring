package kitchenpos.order.infra;

import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("주문이 존재하지 않습니다."));
    }

    default Order getByOrderTable(OrderTable orderTable) {
        return findByOrderTable(orderTable).orElseThrow(() -> new NoSuchElementException("주문이 존재하지 않습니다."));
    }

    Optional<Order> findByOrderTable(OrderTable orderTable);
}
