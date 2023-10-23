package kitchenpos.ui.request;

public class OrderUpdateRequest {

    private String orderStatus;

    public OrderUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderUpdateRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
