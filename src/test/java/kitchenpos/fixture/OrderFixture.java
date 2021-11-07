package kitchenpos.fixture;

import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;

public class OrderFixture {

    private static final Long ID = 1L;
    private static final Long ORDER_TABLE_ID = 1L;
    private static final String ORDER_STATUS = OrderStatus.COOKING.name();
    private static final LocalDateTime ORDERED_TIME = LocalDateTime.now();
    private static final List<OrderLineItem> ORDER_LINE_ITEMS = Collections.singletonList(createOrderLineItem());

    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order createOrder() {
        return createOrder(ID, ORDER_TABLE_ID, ORDER_STATUS, ORDERED_TIME, ORDER_LINE_ITEMS);
    }

    public static Order createOrder(String status) {
        return createOrder(ID, ORDER_TABLE_ID, status, ORDERED_TIME, ORDER_LINE_ITEMS);
    }

    public static OrderRequest createOrderRequest(String status) {
        return new OrderRequest(ORDER_TABLE_ID, status, ORDERED_TIME, ORDER_LINE_ITEMS);
    }

    public static OrderRequest createOrderRequest() {
        return new OrderRequest(ORDER_TABLE_ID, ORDER_STATUS, ORDERED_TIME, ORDER_LINE_ITEMS);
    }

    public static OrderRequest createOrderRequest(List<OrderLineItem> orderLineItems) {
        return new OrderRequest(ORDER_TABLE_ID, ORDER_STATUS, ORDERED_TIME, orderLineItems);
    }

    public static OrderResponse createOrderResponse() {
        return new OrderResponse(ID, ORDER_TABLE_ID, ORDER_STATUS, ORDERED_TIME, ORDER_LINE_ITEMS);
    }
}

class OrderLineItemFixture {

    private static final Long SEQ = 1L;
    private static final Long ORDER_ID = 1L;
    private static final Long MENU_ID = 1L;
    private static final long QUANTITY = 1L;

    public static OrderLineItem createOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(SEQ);
        orderLineItem.setOrderId(ORDER_ID);
        orderLineItem.setMenuId(MENU_ID);
        orderLineItem.setQuantity(QUANTITY);
        return orderLineItem;
    }
}
