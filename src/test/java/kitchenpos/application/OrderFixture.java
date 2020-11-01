package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrderFixture {
    static Order createOrder(Long id, OrderStatus status, Long orderTableId) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        order.setId(id);
        order.setOrderTableId(orderTableId);
        return order;
    }

    static Order createOrderRequest(List<OrderLineItem> orderLineItems, Long orderTableId) {
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        return order;
    }

    static Order updateOrderRequest(OrderStatus status) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        return order;
    }
}
