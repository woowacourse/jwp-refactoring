package kitchenpos.domain.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
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
