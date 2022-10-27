package kitchenpos.dto.request;

public class OrderUpdateStatusRequest {

    private String orderStatus;

    private OrderUpdateStatusRequest() {
    }

    public OrderUpdateStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
