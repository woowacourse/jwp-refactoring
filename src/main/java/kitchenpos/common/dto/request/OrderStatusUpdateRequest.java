package kitchenpos.common.dto.request;

import javax.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull
    private final String orderStatus;

    private OrderStatusUpdateRequest() {
        this(null);
    }

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
