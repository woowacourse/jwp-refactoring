package kitchenpos.ui.dto;

public class OrderUpdateStatusRequest {

    private final String orderStatus;

    public OrderUpdateStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
