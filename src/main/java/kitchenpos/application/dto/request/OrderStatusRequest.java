package kitchenpos.application.dto.request;

public class OrderStatusRequest {
    private final String orderStatus;

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
