package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

  Order save(Order order);

  Optional<Order> findById(Long id);

  List<Order> findAll();

  boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

  boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
