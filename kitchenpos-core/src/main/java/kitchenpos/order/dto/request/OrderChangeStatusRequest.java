package kitchenpos.order.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.order.domain.model.OrderStatus;

public class OrderChangeStatusRequest {

    private final OrderStatus orderStatus;

    @JsonCreator
    public OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
