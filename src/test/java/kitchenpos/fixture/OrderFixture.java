package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {
    public static Order ORDER_ID_STATUS(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    public static Order ORDER(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order ORDER(Long orderTableId, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }

}
