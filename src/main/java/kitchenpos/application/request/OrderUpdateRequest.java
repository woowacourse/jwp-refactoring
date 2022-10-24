package kitchenpos.application.request;

public class OrderUpdateRequest {

    private String orderStatus;

    private OrderUpdateRequest() {
    }

    public OrderUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
