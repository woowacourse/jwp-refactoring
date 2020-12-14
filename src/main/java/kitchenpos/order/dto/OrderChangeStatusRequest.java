package kitchenpos.order.dto;

import java.beans.ConstructorProperties;

public class OrderChangeStatusRequest {
    private final String orderStatus;

    @ConstructorProperties({"orderStatus"})
    public OrderChangeStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
