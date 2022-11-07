package kitchenpos.core.repository.order;

import java.util.List;
import java.util.Optional;
import kitchenpos.core.domain.order.Order;
import kitchenpos.core.domain.order.OrderStatus;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    default Order get(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
