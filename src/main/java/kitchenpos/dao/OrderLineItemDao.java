package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.order.OrderLineItem;

public interface OrderLineItemDao {
    OrderLineItem save(OrderLineItem entity);

    OrderLineItem findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
