package kitchenpos.ui.dto.request;

public class OrdersStatusRequest {

    private String orderStatus;

    private OrdersStatusRequest() {
    }

    public OrdersStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
