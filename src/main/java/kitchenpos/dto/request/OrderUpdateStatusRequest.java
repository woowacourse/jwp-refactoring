package kitchenpos.dto.request;

public class OrderUpdateStatusRequest {

    private final String orderStatus;

    public OrderUpdateStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
