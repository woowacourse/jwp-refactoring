package kitchenpos.dto.order;

import javax.validation.constraints.NotBlank;

public class OrderStatusRequest {

    @NotBlank
    private String orderStatus;

    protected OrderStatusRequest() {
    }

    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
