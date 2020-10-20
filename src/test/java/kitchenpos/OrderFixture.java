package kitchenpos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static Order createOrderWithoutId() {
        final Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(Arrays.asList(createOrderLineItemWithoutOrderId(), createOrderLineItemWithoutOrderId()));
        order.setOrderTableId(1L);

        return order;
    }

    public static Order createOrderWithId(final Long id) {
        final Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setId(id);
        order.setOrderLineItems(Arrays.asList(createOrderLineItemWithOrderId(id), createOrderLineItemWithOrderId(id)));
        order.setOrderTableId(1L);

        return order;
    }

    public static List<Order> createOrders() {
        return Arrays.asList(createOrderWithId(1L), createOrderWithId(2L));
    }

    public static OrderLineItem createOrderLineItemWithoutOrderId() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(2);
        orderLineItem.setSeq(1L);

        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItemWithOrderId(final Long orderId) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(2);
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(orderId);

        return orderLineItem;
    }
}
