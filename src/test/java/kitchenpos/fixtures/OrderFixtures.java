package kitchenpos.fixtures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixtures {

    private static final long ORDER_TABLE_ID = 1L;
    private static final String ORDER_STATUS = "COOKING";
    private static final long ORDER_ID = 1L;
    private static final long MENU_ID = 1L;
    private static final long QUANTITY = 1L;
    private static final long SEQ = 1L;

    public static Order createOrder(
        Long id,
        Long orderTableId,
        String orderStatus,
        List<OrderLineItem> orderLineItems
    ) {
        return new Order(id, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order createOrder() {
        return createOrder(ORDER_ID, ORDER_TABLE_ID, ORDER_STATUS, createOrderLineItems());
    }

    public static Order createOrder(String status) {
        Order order = createOrder();
        order.setOrderStatus(status);
        return order;
    }

    public static OrderRequest createOrderRequest(Order order) {
        return new OrderRequest(order.getOrderTableId(), order.getOrderStatus(), order.getOrderLineItems());
    }

    public static OrderRequest createOrderRequest() {
        return createOrderRequest(createOrder());
    }

    public static List<OrderLineItem> createOrderLineItems() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(createOrderLineItem());
        orderLineItems.add(createOrderLineItem(2L, 1L, 2L, 2L));
        return orderLineItems;
    }

    public static OrderLineItem createOrderLineItem(
        Long seq,
        Long orderId,
        Long menuId,
        long quantity
    ) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem() {
        return createOrderLineItem(SEQ, ORDER_ID, MENU_ID, QUANTITY);
    }
}
