package kitchenpos.order.application.request;

public class OrderStatusUpdateRequest {

    private String orderStatus;

    private OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
