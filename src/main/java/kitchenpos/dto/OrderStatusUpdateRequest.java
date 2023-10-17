package kitchenpos.dto;

public class OrderStatusUpdateRequest {

    private final String orderStatus;

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
