package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByTableIdAndOrderStatusIn(Long tableId, List<OrderStatus> orderStatuses);

    boolean existsByTableIdInAndOrderStatusIn(List<Long> tableIds, List<OrderStatus> orderStatuses);
}
