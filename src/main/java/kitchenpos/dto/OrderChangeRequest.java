package kitchenpos.dto;

public class OrderChangeRequest {

    private String orderStatus;

    public OrderChangeRequest() {
    }

    public OrderChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
