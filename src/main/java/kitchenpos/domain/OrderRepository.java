package kitchenpos.domain;

import static kitchenpos.exception.OrderExceptionType.ORDER_NOT_FOUND;

import java.util.List;
import kitchenpos.exception.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderException(ORDER_NOT_FOUND));
    }

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
