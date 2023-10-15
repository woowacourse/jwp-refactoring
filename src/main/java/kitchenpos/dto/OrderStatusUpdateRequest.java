package kitchenpos.dto;

public class OrderStatusUpdateRequest {

    private String orderStatus;

    protected OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
