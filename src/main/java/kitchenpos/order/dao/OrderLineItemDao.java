package kitchenpos.order.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderLineItem;

public interface OrderLineItemDao {
    OrderLineItem save(OrderLineItem entity, Long orderId);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
