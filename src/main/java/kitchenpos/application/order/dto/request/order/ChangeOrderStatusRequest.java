package kitchenpos.application.order.dto.request.order;

import kitchenpos.domain.order.OrderStatus;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
