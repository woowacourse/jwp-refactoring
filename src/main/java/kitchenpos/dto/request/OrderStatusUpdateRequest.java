package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull
    private final String orderStatus;

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
