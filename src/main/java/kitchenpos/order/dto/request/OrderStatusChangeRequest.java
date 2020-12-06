package kitchenpos.order.dto.request;

import com.sun.istack.NotNull;

public class OrderStatusChangeRequest {
    @NotNull
    private String orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
