package kitchenpos;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

import java.time.LocalDateTime;

import static kitchenpos.domain.order.OrderStatus.MEAL;

public class OrderFixture {

    private static final OrderStatus ORDER_STATUS = MEAL;
    private static final LocalDateTime ORDER_TIME = LocalDateTime.now();

    public static Order createOrder() {
        return createOrder(null);
    }

    public static Order createOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(ORDER_STATUS.name());
        order.setOrderedTime(ORDER_TIME);
        return order;
    }

    public static OrderLineItem createOrderLineItem() {
        return createOrderLineItem(null);
    }

    public static OrderLineItem createOrderLineItem(Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }
}
