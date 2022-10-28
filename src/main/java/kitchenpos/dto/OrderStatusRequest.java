package kitchenpos.dto;

public class OrderStatusRequest {

    private final String orderStatus;

    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
