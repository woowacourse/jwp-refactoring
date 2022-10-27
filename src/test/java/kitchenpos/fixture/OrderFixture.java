package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order createOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        return order;
    }

    public static Order createOrder(String orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order createOrder(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                              List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
