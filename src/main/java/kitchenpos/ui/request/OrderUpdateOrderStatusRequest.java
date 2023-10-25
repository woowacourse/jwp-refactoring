package kitchenpos.ui.request;

public class OrderUpdateOrderStatusRequest {

    private String orderStatus;

    public OrderUpdateOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderUpdateOrderStatusRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
