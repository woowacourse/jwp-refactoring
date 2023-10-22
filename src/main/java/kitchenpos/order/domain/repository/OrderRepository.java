package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public interface OrderRepository {

  Order save(final Order entity);

  Optional<Order> findById(final Long id);

  List<Order> findAll();

  boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
      final List<OrderStatus> orderStatuses);

  boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
      final List<OrderStatus> orderStatuses);
}
