package kitchenpos.ui.dto;

public class OrderChangeStatusRequest {

    private String orderStatus;

    public OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
