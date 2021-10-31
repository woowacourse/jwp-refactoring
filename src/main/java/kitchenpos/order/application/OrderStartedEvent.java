package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderStartedEvent {
    private Order order;
    private List<OrderLineItem> orderLineItems;
    private List<Long> menuIds;

    public OrderStartedEvent(Order order, List<OrderLineItem> orderLineItems, List<Long> menuIds) {
        this.order = order;
        this.orderLineItems = new ArrayList<>(orderLineItems);
        this.menuIds = new ArrayList<>(menuIds);
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public List<Long> getMenuIds() {
        return Collections.unmodifiableList(menuIds);
    }
}
