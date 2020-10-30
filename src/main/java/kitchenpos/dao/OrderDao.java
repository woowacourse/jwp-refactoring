package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.Order;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByTableIdAndOrderStatusIn(Long tableId, List<String> orderStatuses);

    boolean existsByTableIdInAndOrderStatusIn(List<Long> tableIds, List<String> orderStatuses);
}
