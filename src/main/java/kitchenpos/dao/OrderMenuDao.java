package kitchenpos.dao;

import kitchenpos.domain.OrderMenu;

import java.util.List;
import java.util.Optional;

public interface OrderMenuDao {
    OrderMenu save(OrderMenu entity);

    Optional<OrderMenu> findById(Long id);

    List<OrderMenu> findAll();

    List<OrderMenu> findAllByOrderId(Long orderId);
}
