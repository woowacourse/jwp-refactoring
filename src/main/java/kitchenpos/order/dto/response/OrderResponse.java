package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Order order) {
        id = order.getId();
        orderTableId = order.getOrderTableId();
        orderStatus = order.getOrderStatus();
        orderedTime = order.getOrderedTime();
        orderLineItems = order.getOrderLineItems().stream()
            .map(it -> new OrderLineItemResponse(it))
            .collect(Collectors.toList());
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
