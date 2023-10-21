package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;

public interface OrderRepository {

  Order save(final Order entity);

  Optional<Order> findById(final Long id);

  List<Order> findAll();

  boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
      final List<String> orderStatuses);

  boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
      final List<String> orderStatuses);
}
