package kitchenpos.dao;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTable_IdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTable_IdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
