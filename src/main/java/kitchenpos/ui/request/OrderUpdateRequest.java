package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderUpdateRequest {

    @NotNull
    private String orderStatus;

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

}
