package fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
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
        builder.orderLineItems = List.of();
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
        final Order order = new Order(
                this.id,
                this.orderTable,
                this.orderStatus,
                this.orderedTime
        );
        orderLineItems.forEach(order::addOrderLineItem);
        return order;
    }
}
