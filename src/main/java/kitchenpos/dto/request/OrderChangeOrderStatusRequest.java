package kitchenpos.dto.request;

public class OrderChangeOrderStatusRequest {

    private final String orderStatus;

    public OrderChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
