package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTableResponse;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    private OrderResponse() {
    }

    private OrderResponse(final Long id,
                          final OrderTableResponse orderTableResponse,
                          final OrderStatus orderStatus,
                          final LocalDateTime orderedTime,
                          final List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse of(final Order order,
                                   final OrderTable orderTable,
                                   final List<OrderLineItem> orderLineItems) {

        List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(),
                OrderTableResponse.from(orderTable),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponses);
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
