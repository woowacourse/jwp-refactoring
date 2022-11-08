package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;

public interface OrderRepository {
    Order save(Order entity);

    Order findById(Long id);

    List<Order> findAll();

    void validateComplete(List<Long> orderTableIds);

    void validateOrdersCompleted(Long orderTableId);
}
