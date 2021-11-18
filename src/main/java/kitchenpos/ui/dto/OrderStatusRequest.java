package kitchenpos.ui.dto;

import javax.validation.constraints.NotBlank;

public class OrderStatusRequest {

    @NotBlank
    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
