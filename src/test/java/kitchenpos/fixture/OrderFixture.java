package kitchenpos.fixture;

import static kitchenpos.core.order.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.order.domain.OrderStatus;

public class OrderFixture {

    public static Order getOrderRequest(final Long orderTableId) {
        return getOrderRequest(orderTableId, Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrderRequest(final String orderStatus) {
        final Order orderRequest = getOrderRequest(1L, Arrays.asList(getOrderLineItemRequest()));
        orderRequest.changeOrderStatus(OrderStatus.from(orderStatus));
        return orderRequest;
    }

    public static Order getOrderRequest() {
        return getOrderRequest(1L, Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrderRequest(final Long orderTableId,
                                        final List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems);
    }

    public static Order getOrder(final OrderStatus OrderStatus) {
        return getOrder(1L, 1L, OrderStatus, LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrder() {
        return getOrder(1L, 1L, COOKING, LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrder(final Long id,
                                 final Long orderTableId,
                                 final OrderStatus orderStatus,
                                 final LocalDateTime orderedTime,
                                 final List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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
