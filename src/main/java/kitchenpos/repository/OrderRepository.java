package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;

public interface OrderRepository {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findByOrderTableId(Long orderTableId);

    List<Order> findAll();
}
