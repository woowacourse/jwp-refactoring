package kitchenpos.domain;

import java.util.List;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void setOrder(Order order) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(order);
        }
    }

    public List<OrderLineItem> toList() {
        return orderLineItems;
    }
}
