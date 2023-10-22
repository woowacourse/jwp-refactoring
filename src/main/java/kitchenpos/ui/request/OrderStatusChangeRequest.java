package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderStatusChangeRequest {

    @NotNull
    private final String orderStatus;

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
