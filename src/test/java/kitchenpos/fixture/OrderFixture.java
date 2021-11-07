package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OrderFixture {

    private static final Long ID = 1L;
    private static final Long ORDER_TABLE_ID = 1L;
    private static final String ORDER_STATUS = OrderStatus.COOKING.name();
    private static final LocalDateTime ORDERED_TIME = LocalDateTime.now();

    private static final Long SEQ = 1L;
    private static final Long ORDER_ID = 1L;
    private static final Long MENU_ID = 1L;
    private static final long QUANTITY = 1L;

    public static Order create() {
        return create(ID, ORDER_TABLE_ID, ORDER_STATUS, ORDERED_TIME, orderLineItems());
    }

    public static Order create(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static OrderLineItem orderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(SEQ);
        orderLineItem.setOrderId(ORDER_ID);
        orderLineItem.setMenuId(MENU_ID);
        orderLineItem.setQuantity(QUANTITY);

        return orderLineItem;
    }

    public static List<OrderLineItem> orderLineItems() {
        return Collections.singletonList(orderLineItem());
    }
}
