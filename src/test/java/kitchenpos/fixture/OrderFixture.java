package kitchenpos.fixture;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order getOrderRequest(final Long orderTableId) {
        return getOrderRequest(orderTableId, COOKING.name(), LocalDateTime.now(), new LinkedList<>());
    }

    public static Order getOrderRequest(final String orderStatus) {
        return getOrderRequest(1L, orderStatus, LocalDateTime.now(), new LinkedList<>());
    }

    public static Order getOrderRequest() {
        return getOrderRequest(1L, COOKING.name(), LocalDateTime.now(), new LinkedList<>());
    }

    public static Order getOrderRequest(final Long orderTableId,
                                        final String orderStatus,
                                        final LocalDateTime localDateTime,
                                        final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, localDateTime, orderLineItems);
    }

    public static Order getOrder(final String OrderStatus) {
        return getOrder(1L, 1L, OrderStatus, LocalDateTime.now(), new LinkedList<>());
    }

    public static Order getOrder() {
        return getOrder(1L, 1L, COOKING.name(), LocalDateTime.now(), new LinkedList<>());
    }

    public static Order getOrder(final Long id,
                                 final Long orderTableId,
                                 final String orderStatus,
                                 final LocalDateTime orderedTime,
                                 final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderLineItem getOrderLineItem(final Long orderId) {
        return getOrderLineItem(1L, orderId, 1L, 1);
    }

    public static OrderLineItem getOrderLineItem(final Long seq,
                                                 final Long orderId,
                                                 final Long menuId,
                                                 final long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public static OrderLineItem getOrderLineItemRequest() {
        return getOrderLineItemRequest(1L, 1L, 2);
    }

    public static OrderLineItem getOrderLineItemRequest(final Long orderId,
                                                        final Long menuId,
                                                        final long quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
