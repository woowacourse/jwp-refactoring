package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTableLog;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private Long id;
    private OrderTableLogResponse orderTableLogResponse;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
    }

    private OrderResponse(
            Long id,
            OrderTableLogResponse orderTableLogResponse,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableLogResponse = orderTableLogResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order, OrderTableLog orderTableLog, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(
                order.getId(),
                OrderTableLogResponse.from(orderTableLog),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItems.stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public OrderTableLogResponse getOrderTableLogResponse() {
        return orderTableLogResponse;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
