package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static Order createOrder(Long id, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems, OrderStatus orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setId(id);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(orderStatus != null ? orderStatus.name() : null);
        order.setOrderTableId(orderTableId);

        return order;
    }

    public static Order createOrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return createOrder(null, null, orderLineItems, null, orderTableId);
    }

    public static Order createOrderRequestChangeOrderStatus(OrderStatus orderStatus) {
        return createOrder(null, null, null, orderStatus, null);
    }
}
