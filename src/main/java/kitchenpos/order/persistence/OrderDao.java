package kitchenpos.order.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.application.dto.OrderPersistence;

public interface OrderDao {

  OrderPersistence save(OrderPersistence entity);

  Optional<OrderPersistence> findById(Long id);

  List<OrderPersistence> findAll();

  boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

  boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds,
      List<String> orderStatuses);
}
