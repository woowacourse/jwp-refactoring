package support.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order_line_item.OrderLineItem;
import kitchenpos.domain.order_table.OrderTable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OrderBuilder {

    private final Order order;

    public OrderBuilder() {
        this.order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(Collections.emptyList());
    }

    public OrderBuilder setOrderedTime(final LocalDateTime orderTime) {
        order.setOrderedTime(orderTime);
        return this;
    }

    public OrderBuilder setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        order.setOrderLineItems(orderLineItems);
        return this;
    }

    public OrderBuilder setOrderTableId(final OrderTable orderTable) {
        order.setOrderTable(orderTable);
        return this;
    }

    public OrderBuilder setOrderStatus(final OrderStatus orderStatus) {
        order.setOrderStatus(orderStatus.name());
        return this;
    }

    public Order build() {
        return order;
    }
}
