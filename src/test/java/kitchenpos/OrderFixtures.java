package kitchenpos;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.OrderChangeRequest;
import kitchenpos.application.dto.OrderLineItemResponse;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixtures {

    private OrderFixtures() {
    }

    public static Order createOrder() {
        return new Order(1L, null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));
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
        return new OrderLineItemResponse(1L, 1L, 1L, 2);
    }

    public static OrderRequest createOrderRequest() {
        return new OrderRequest(null, null);
    }

    public static OrderChangeRequest createOrderChangeRequest() {
        return null;
    }
}
