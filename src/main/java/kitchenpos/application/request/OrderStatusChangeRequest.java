package kitchenpos.application.request;

public class OrderStatusChangeRequest {

    private String orderStatus;

    private OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
