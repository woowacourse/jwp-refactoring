package kitchenpos.order.ui.dto;

import javax.validation.constraints.NotNull;

public class OrderChangeStatusRequest {

    @NotNull
    private String orderStatus;

    private OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
