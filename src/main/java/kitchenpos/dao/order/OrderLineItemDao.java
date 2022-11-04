package kitchenpos.dao.order;

import kitchenpos.domain.order.OrderLineItem;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemDao {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
