package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public interface OrderRepository {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
