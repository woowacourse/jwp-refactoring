package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.OrderLineItemEntity;
import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemDao2 {
    OrderLineItemEntity save(OrderLineItemEntity entity);

    Optional<OrderLineItemEntity> findById(Long id);

    List<OrderLineItemEntity> findAll();

    List<OrderLineItemEntity> findAllByOrderId(Long orderId);
}
