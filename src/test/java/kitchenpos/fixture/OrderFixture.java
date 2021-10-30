package kitchenpos.fixture;

import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    private static final Long ID = 1L;
    private static final Long ORDER_TABLE_ID = 1L;
    private static final String ORDER_STATUS = OrderStatus.COOKING.name();
    private static final LocalDateTime ORDERED_TIME = LocalDateTime.now();
    private static final List<OrderLineItem> ORDER_LINE_ITEMS = Collections.singletonList(createOrderLineItem());

    public static Order createOrder() {
        Order order = new Order();
        order.setId(ID);
        order.setOrderTableId(ORDER_TABLE_ID);
        order.setOrderStatus(ORDER_STATUS);
        order.setOrderedTime(ORDERED_TIME);
        order.setOrderLineItems(ORDER_LINE_ITEMS);
        return order;
    }

    public static Order createOrder(OrderStatus status) {
        Order order = new Order();
        order.setId(ID);
        order.setOrderTableId(ORDER_TABLE_ID);
        order.setOrderStatus(status.name());
        order.setOrderedTime(ORDERED_TIME);
        order.setOrderLineItems(ORDER_LINE_ITEMS);
        return order;
    }

    public static Order createOrder(Long id, String status) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(ORDER_TABLE_ID);
        order.setOrderStatus(status);
        order.setOrderedTime(ORDERED_TIME);
        order.setOrderLineItems(ORDER_LINE_ITEMS);
        return order;
    }
}

class OrderLineItemFixture {

    private static final Long SEQ = 1L;
    private static final Long ORDER_ID = 1L;
    private static final Long MENU_ID = 1L;
    private static final long QUANTITY = 1L;

    public static OrderLineItem createOrderLineItem(){
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(SEQ);
        orderLineItem.setOrderId(ORDER_ID);
        orderLineItem.setMenuId(MENU_ID);
        orderLineItem.setQuantity(QUANTITY);
        return orderLineItem;
    }
}
