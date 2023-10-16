package kitchenpos.common.fixtures;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixtures {

    /**
     * ORDER_TABLE_ID
     */
    public static final Long ORDER1_ORDER_TABLE_ID = 1L;

    /**
     * ORDER_LINE_ITEM
     */
    public static List<OrderLineItem> ORDER1_ORDER_LINE_ITEMS() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        return List.of(orderLineItem);
    }

    /**
     * REQUEST
     */
    public static Order ORDER1_CREATE_REQUEST() {
        Order order = new Order();
        order.setOrderTableId(ORDER1_ORDER_TABLE_ID);
        order.setOrderLineItems(ORDER1_ORDER_LINE_ITEMS());
        return order;
    }
}
