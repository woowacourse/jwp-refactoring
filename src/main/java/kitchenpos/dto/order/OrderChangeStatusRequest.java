package kitchenpos.dto.order;

public class OrderChangeStatusRequest {

    private final String orderStatus;

    public OrderChangeStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
