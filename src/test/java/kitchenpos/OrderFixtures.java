package kitchenpos;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixtures {

    private OrderFixtures() {
    }

    public static Order createOrder() {
        return createOrder(OrderStatus.COOKING.name());
    }

    public static Order createOrder(OrderTable orderTable) {
        return createOrder(orderTable, OrderStatus.COOKING.name());
    }

    public static Order createOrder(String orderStatus) {
        return createOrder(OrderTableFixtures.createOrderTable(1L, null, 2, false), orderStatus);
    }

    public static Order createOrder(OrderTable orderTable, String orderStatus) {
        return new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(new OrderLineItem(1L, 2)));
    }

    public static OrderResponse createOrderResponse() {
        return new OrderResponse(
                1L,
                1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(createOrderLineItemResponse())
        );
    }

    private static OrderLineItemResponse createOrderLineItemResponse() {
        return new OrderLineItemResponse(1L, 1L, 2);
    }

    public static OrderRequest createOrderRequest() {
        return createOrderRequest(List.of(createOrderLineItemRequest(1L), createOrderLineItemRequest(2L)));
    }

    public static OrderRequest createOrderRequest(Long orderTableId) {
        return new OrderRequest(orderTableId, List.of(createOrderLineItemRequest(1L), createOrderLineItemRequest(2L)));
    }

    public static OrderRequest createOrderRequest(List<OrderLineItemRequest> itemRequests) {
        return new OrderRequest(1L, itemRequests);
    }

    public static OrderRequest createOrderRequest(Long orderTableId, List<OrderLineItemRequest> itemRequests) {
        return new OrderRequest(orderTableId, itemRequests);
    }

    public static OrderLineItemRequest createOrderLineItemRequest() {
        return createOrderLineItemRequest(1L);
    }

    public static OrderLineItemRequest createOrderLineItemRequest(Long menuId) {
        return new OrderLineItemRequest(null, menuId, 2);
    }

    public static OrderStatusChangeRequest createOrderChangeRequest() {
        return new OrderStatusChangeRequest(OrderStatus.MEAL.name());
    }
}
