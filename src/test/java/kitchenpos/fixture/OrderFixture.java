package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {
    public static Order createOrder(Long id, OrderStatus status, Long orderTableId, LocalDateTime orderedTime) {
        Order order = createOrder(id, status, orderTableId);
        order.setOrderedTime(orderedTime);
        return order;
    }

    public static Order createOrder(Long id, OrderStatus status, Long orderTableId) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        order.setId(id);
        order.setOrderTableId(orderTableId);
        return order;
    }

    public static Order createOrderRequest(List<OrderLineItem> orderLineItems, Long orderTableId) {
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        return order;
    }

    public static Order updateOrderRequest(OrderStatus status) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        return order;
    }
}
