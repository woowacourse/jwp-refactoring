package kitchenpos.domain;

import java.util.List;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems, Order order) {
        this.orderLineItems = updateOrder(orderLineItems, order);
    }

    private List<OrderLineItem> updateOrder(List<OrderLineItem> orderLineItems, Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(order));
        return orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
