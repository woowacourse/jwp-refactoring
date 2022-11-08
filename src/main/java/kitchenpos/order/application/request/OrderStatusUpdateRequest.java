package kitchenpos.order.application.request;

public class OrderStatusUpdateRequest {

    private final Long orderId;
    private final String orderStatus;

    public OrderStatusUpdateRequest(Long orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
