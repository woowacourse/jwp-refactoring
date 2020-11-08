package kitchenpos.factory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.entity.Order;

@Component
public class OrderFactory {
    public Order create(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public Order create(String orderStatus) {
        return create(null, null, orderStatus, null, null);
    }

    public Order create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return create(null, orderTableId, null, null, orderLineItems);
    }

    public Order create(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return create(id, orderTableId, null, null, orderLineItems);
    }

    public Order create(Long id) {
        return create(id, null, null, null, null);
    }
}
