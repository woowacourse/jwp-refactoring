package kitchenpos.order.domain;

import static kitchenpos.order.exception.OrderExceptionType.ORDER_NOT_FOUND;

import java.util.List;
import kitchenpos.order.exception.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderException(ORDER_NOT_FOUND));
    }

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);

    List<Order> findAllByOrderTableId(Long orderTableId);
}
