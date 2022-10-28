package kitchenpos.ui.dto.request;

public class OrderStatusRequest {

    private String orderStatus;

    private OrderStatusRequest() {}

    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
