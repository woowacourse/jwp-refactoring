package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.order.OrderStatus;

public class OrderStatusRequest {
    @NotNull
    private OrderStatus orderStatus;

    private OrderStatusRequest() {
    }

    public OrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
