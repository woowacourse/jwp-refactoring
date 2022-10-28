package kitchenpos.ui.dto;

public class OrderStatusUpdateRequest {

    private final String orderStatus;

    public OrderStatusUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
