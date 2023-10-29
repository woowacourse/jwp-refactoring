package kitchenpos.application.request;

public class OrderStatusRequest {
    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
