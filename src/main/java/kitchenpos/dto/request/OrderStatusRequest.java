package kitchenpos.dto.request;

import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {

    private String orderStatus;

    private OrderStatusRequest() {
    }

    public OrderStatusRequest(final OrderStatus orderStatus) {
        this(orderStatus.name());
    }

    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
