package kitchenpos.application.order.request;

public class OrderUpdateRequest {

    private String orderStatus;

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
