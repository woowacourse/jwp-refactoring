package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import kitchenpos.order.application.request.OrderStatusUpdateRequest;

public class OrderStatusUpdateApiRequest {

    private final String orderStatus;

    @JsonCreator
    public OrderStatusUpdateApiRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatusUpdateRequest toServiceRequest(Long orderId) {
        return new OrderStatusUpdateRequest(orderId, orderStatus);
    }
}
