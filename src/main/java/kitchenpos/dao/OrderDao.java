package kitchenpos.dao;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdsAndOrderStatuses(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
