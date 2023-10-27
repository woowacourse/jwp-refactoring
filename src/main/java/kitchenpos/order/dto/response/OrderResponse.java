package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
                .map(each -> OrderLineItemResponse.from(order.getId(), each))
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), orderLineItemResponses);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
