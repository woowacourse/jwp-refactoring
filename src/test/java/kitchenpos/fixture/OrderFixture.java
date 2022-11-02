package kitchenpos.fixture;

import static kitchenpos.core.order.domain.OrderStatus.COOKING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.order.application.dto.OrderLineItemRequest;
import kitchenpos.core.order.application.dto.OrderRequest;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.order.domain.OrderStatus;

public class OrderFixture {

    public static OrderRequest getOrderRequest(final Long orderTableId) {
        return getOrderRequest(orderTableId, COOKING.name(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static OrderRequest getOrderRequest(final Long orderTableId, final String orderStatus) {
        return getOrderRequest(orderTableId, orderStatus, Arrays.asList(getOrderLineItemRequest()));
    }

    public static OrderRequest getOrderRequest(final String orderStatus) {
        return getOrderRequest(1L, orderStatus, Arrays.asList(getOrderLineItemRequest()));
    }

    public static OrderRequest getOrderRequest() {
        return getOrderRequest(1L, COOKING.name(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static OrderRequest getOrderRequest(final Long orderTableId,
                                        final String orderStatus,
                                        final List<OrderLineItemRequest> orderLineItemsRequest) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItemsRequest);
    }

    public static Order getOrder(final OrderStatus OrderStatus) {
        return getOrder(1L, 1L, OrderStatus, LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrder(final String orderStatus) {
        return getOrder(1L, 1L, OrderStatus.from(orderStatus), LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrder() {
        return getOrder(1L, 1L, COOKING, LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getUnSavedOrder(final String orderStatus) {
        return getOrder(null, 1L, OrderStatus.from(orderStatus), LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getUnSavedOrder() {
        return getOrder(null, 1L, COOKING, LocalDateTime.now(), Arrays.asList(getOrderLineItemRequest()));
    }

    public static Order getOrder(final Long id,
                                 final Long orderTableId,
                                 final OrderStatus orderStatus,
                                 final LocalDateTime orderedTime,
                                 final List<OrderLineItemRequest> orderLineItemsRequest) {
        final List<OrderLineItem> orderLineItems = mapToOrderLineItems(id, orderLineItemsRequest);
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems, order -> {});
    }

    private static List<OrderLineItem> mapToOrderLineItems(final Long orderId, final List<OrderLineItemRequest> orderLineItemsRequest) {
        return orderLineItemsRequest.stream()
                .map(it -> new OrderLineItem(orderId, it.getQuantity(), "추천메뉴", BigDecimal.valueOf(10000)))
                .collect(Collectors.toList());
    }

    public static OrderLineItem getOrderLineItem() {
        return getOrderLineItem(1L, 1L, 1, "추천메뉴", BigDecimal.valueOf(10000));
    }

    public static OrderLineItem getOrderLineItem(final Long orderId) {
        return getOrderLineItem(1L, orderId, 1, "추천메뉴", BigDecimal.valueOf(10000));
    }

    public static OrderLineItem getOrderLineItem(final Long seq,
                                                 final Long orderId,
                                                 final long quantity,
                                                 final String name,
                                                 final BigDecimal price) {
        return new OrderLineItem(seq, orderId, quantity, name, price);
    }

    public static OrderLineItemRequest getOrderLineItemRequest() {
        return getOrderLineItemRequest(1L, 2);
    }

    public static OrderLineItemRequest getOrderLineItemRequest(final Long menuId,
                                                               final long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
