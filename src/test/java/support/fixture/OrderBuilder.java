package support.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order_line_item.OrderLineItem;
import kitchenpos.domain.order_table.OrderTable;

import java.util.Collections;
import java.util.List;

public class OrderBuilder {

    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private List<OrderLineItem> orderLineItems;

    public OrderBuilder() {
        this.orderTable = null;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = Collections.emptyList();
    }

    public OrderBuilder setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public OrderBuilder setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
        return this;
    }

    public OrderBuilder setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Order build() {
        return new Order(orderTable, orderStatus, orderLineItems);
    }
}
