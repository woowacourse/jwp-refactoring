package kitchenpos.order.persistence;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemDao {

    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
