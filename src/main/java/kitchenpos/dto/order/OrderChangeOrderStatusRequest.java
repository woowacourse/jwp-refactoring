package kitchenpos.dto.order;

import kitchenpos.domain.OrderStatus;

public class OrderChangeOrderStatusRequest {

    private String orderStatus;

    private OrderChangeOrderStatusRequest() {
    }

    public OrderChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
