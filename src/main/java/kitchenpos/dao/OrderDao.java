package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findByTableIds(List<Long> tableIds);

    List<Order> findAll();

    boolean existsByTableIdAndOrderStatusIn(Long tableId, List<OrderStatus> orderStatuses);
}
