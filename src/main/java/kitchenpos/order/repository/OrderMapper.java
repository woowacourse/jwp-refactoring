package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order mapOrder(Order entity, List<OrderLineItem> orderLineItems) {
        entity.placeOrderLineItems(new OrderLineItems(orderLineItems));
        entity.placeOrderId();
        return entity;
    }
}
