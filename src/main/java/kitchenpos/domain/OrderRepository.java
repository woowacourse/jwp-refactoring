package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

  Order2 save(Order2 order);

  Optional<Order2> findById(Long id);

  List<Order2> findAll();

  boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

  boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
