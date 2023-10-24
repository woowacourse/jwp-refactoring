package kitchenpos.domain.repository;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
