package kitchenpos.ordertable.dto.request;

public class OrderStatusChangeRequest {

    private final String orderStatus;

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
