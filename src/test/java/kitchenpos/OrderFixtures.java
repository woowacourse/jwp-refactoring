package kitchenpos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderLineItemResponse;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixtures {

    private OrderFixtures() {
    }

    public static Order createOrder() {
        return createOrder(OrderStatus.COOKING.name());
    }

    public static Order createOrder(String orderStatus) {
        return new Order(1L, orderStatus, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));
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
