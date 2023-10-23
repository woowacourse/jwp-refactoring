package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.OrderLineItemEntity;

public interface OrderLineItemDao {
    OrderLineItemEntity save(OrderLineItemEntity entity);

    Optional<OrderLineItemEntity> findById(Long id);

    List<OrderLineItemEntity> findAll();

    List<OrderLineItemEntity> findAllByOrderId(Long orderId);
}
