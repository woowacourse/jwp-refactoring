package kitchenpos.order.application;

import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);

    Optional<Order> findByOrderTableId(Long orderTableId);

    default Order getById(final Long id) {
        return findById(id).orElseThrow(() -> new OrderException(ORDER_IS_NOT_FOUND));
    }
}
