package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.order.Order;

public interface OrderDao {
    Order save(Order entity);

    Order findById(Long id);

    List<Order> findAll();

    void validateComplete(List<Long> orderTableIds);

    void validateOrdersCompleted(Long orderTableId);
}
