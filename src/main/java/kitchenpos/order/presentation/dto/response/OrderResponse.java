package kitchenpos.order.presentation.dto.response;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public OrderResponse(final Long id,
                         final Long orderTableId,
                         final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItemResponses(order)
        );
    }

    private static List<OrderLineItemResponse> orderLineItemResponses(Order order) {
        return order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::new)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemResponse> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", orderTableId=" + orderTableId +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderedTime=" + orderedTime +
                '}';
    }
}
