package kitchenpos.ui.dto.request;

public class OrdersStatusRequest {

    private String orderStatus;

    public OrdersStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
