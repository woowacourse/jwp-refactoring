package fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    private Long id;
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public static OrderBuilder init() {
        final OrderBuilder builder = new OrderBuilder();
        builder.id = null;
        builder.orderTable = OrderTableBuilder.init().build();
        builder.orderStatus = OrderStatus.COOKING;
        builder.orderedTime = LocalDateTime.now();
        builder.orderLineItems = new ArrayList<>();
        return builder;
    }

    public OrderBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
        return this;
    }

    public OrderBuilder orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder orderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public Order build() {
        final Order order = Order.create(this.orderTable, orderLineItems);
        order.changeOrderStatus(orderStatus);
        return order;
    }
}
