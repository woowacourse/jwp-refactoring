package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OrderFixture {

    public static Order createOrder(Long id, String orderStatus, Long tableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(orderStatus);
        order.setOrderTableId(tableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order createOrderWithId(Long id) {
        Order order = new Order();
        order.setId(id);
        return order;
    }

    public static Order createOrderWithoutId(Long tableId, OrderLineItem orderLineItem) {
        return createOrder(null, null, tableId, Arrays.asList(orderLineItem));
    }

    public static Order createOrderWithoutId(Long tableId, String orderStatus, OrderLineItem orderLineItem) {
        return createOrder(null, orderStatus, tableId, Arrays.asList(orderLineItem));
    }

    public static Order createOrderWithOrderStatus(String orderStatus) {
        return createOrder(null, orderStatus, null, null);
    }

    public static Order createOrderWithOrderStatusAndTableId(String orderStatus, Long tableId) {
        return createOrder(null, orderStatus, tableId, null);
    }

    public static Order createOrderEmptyOrderLineItem() {
        return createOrder(null, null, null, null);
    }
}
