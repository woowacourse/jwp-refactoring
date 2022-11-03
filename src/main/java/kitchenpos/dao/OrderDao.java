package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Order;

public interface OrderDao {
    Order save(Order entity);

    Order findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
