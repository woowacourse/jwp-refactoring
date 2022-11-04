package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;

public interface OrderLineItemRepository {
    OrderLineItem save(OrderLineItem entity);

    OrderLineItem findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
