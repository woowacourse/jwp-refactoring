package kitchenpos.order.application.dto.request.order;

import kitchenpos.order.domain.OrderStatus;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
