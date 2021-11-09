package kitchenpos.application.dtos;

public class OrderStatusRequest {
    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
