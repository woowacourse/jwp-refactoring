package order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import order.domain.Order;
import order.domain.OrderLineItem;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    public static OrderResponse of(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemResponse.toList(orderLineItems)
        );
    }

    protected OrderResponse() {
    }

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                          List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }

    public void setOrderLineItemResponses(final List<OrderLineItemResponse> orderLineItemResponses) {
        this.orderLineItemResponses = orderLineItemResponses;
    }
}
