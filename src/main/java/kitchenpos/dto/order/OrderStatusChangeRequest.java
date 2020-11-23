package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;

public class OrderStatusChangeRequest {
    private String orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus toOrderStatus() {
        return OrderStatus.from(this.orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
