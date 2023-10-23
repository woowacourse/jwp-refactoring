package kitchenpos.dto;

public class OrderUpdateStatusRequest {

    private String orderStatus;

    public OrderUpdateStatusRequest() {
    }

    public OrderUpdateStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
