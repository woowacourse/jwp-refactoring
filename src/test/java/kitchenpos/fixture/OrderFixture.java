package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order 주문(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    public static Order 주문(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
