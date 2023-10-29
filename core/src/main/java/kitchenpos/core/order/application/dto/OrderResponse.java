package kitchenpos.core.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderStatus;

public class OrderResponse {
    @JsonProperty
    private final Long id;
    @JsonProperty
    private final Long orderTableId;
    @JsonProperty
    private final OrderStatus orderStatus;
    @JsonProperty
    private final LocalDateTime orderedTime;
    @JsonProperty
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemResponse.from(order.getOrderLineItems())
        );
    }

    public static List<OrderResponse> from(final List<Order> orders) {
        return orders
                .stream().map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }
}
