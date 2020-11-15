package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order createWithStatus(Long orderTableId, OrderStatus status) {
        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(status.name());
        return order;
    }

    public static Order createEmptyOrderLines() {
        return new Order();
    }

    public static Order createWithOrderLines(List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}
