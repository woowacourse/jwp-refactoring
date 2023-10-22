package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Order toEntity() {
        final Order order = new Order();
        order.setOrderStatus(orderStatus);

        return order;
    }
}
