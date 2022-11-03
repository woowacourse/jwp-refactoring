package kitchenpos.dao;

import kitchenpos.domain.order.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Orders save(Orders entity);

    Optional<Orders> findById(Long id);

    List<Orders> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
