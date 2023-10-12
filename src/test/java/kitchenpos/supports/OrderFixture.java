package kitchenpos.supports;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    private Long id = null;
    private Long orderTableId = 1L;
    private String orderStatus = "COOKING";
    private LocalDateTime orderedTime = LocalDateTime.of(2023, 10, 2, 10, 0);
    private List<OrderLineItem> orderLineItems;

    private OrderFixture() {
    }

    public static OrderFixture fixture() {
        return new OrderFixture();
    }

    public OrderFixture id(Long id) {
        this.id = id;
        return this;
    }

    public OrderFixture orderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    public OrderFixture orderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderFixture orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.toString();
        return this;
    }


    public OrderFixture orderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderFixture orderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public Order build() {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
