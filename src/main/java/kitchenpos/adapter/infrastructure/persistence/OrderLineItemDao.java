package kitchenpos.adapter.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.model.order.OrderLineItem;

public interface OrderLineItemDao {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
