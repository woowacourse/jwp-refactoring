package kitchenpos.generator;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderGenerator {

    public static Order newInstance(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order newInstance(String orderStatus) {
        return newInstance(null, orderStatus, null);
    }

    public static Order newInstance(
        Long orderTableId,
        String orderStatus,
        LocalDateTime orderedTime
    ) {
        return newInstance(null, orderTableId, orderStatus, orderedTime);
    }

    public static Order newInstance(
        Long id,
        Long orderTableId,
        String orderStatus,
        LocalDateTime orderedTime
    ) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        return order;
    }

    public static OrderLineItem newOrderLineItem(Long menuId, long quantity) {
        return newOrderLineItem(null, menuId, quantity);
    }

    public static OrderLineItem newOrderLineItem(Long orderId, Long menuId, long quantity) {
        return newOrderLineItem(null, orderId, menuId, quantity);
    }

    public static OrderLineItem newOrderLineItem(
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
}
