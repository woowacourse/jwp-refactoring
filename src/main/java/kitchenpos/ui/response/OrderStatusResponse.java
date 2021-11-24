package kitchenpos.ui.response;

import kitchenpos.domain.OrderStatus;

public class OrderStatusResponse {

    private String orderStatus;

    public OrderStatusResponse() {
    }

    public OrderStatusResponse(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    private static OrderStatusResponse from(final OrderStatus orderStatus) {
        return new OrderStatusResponse(orderStatus.name());
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
