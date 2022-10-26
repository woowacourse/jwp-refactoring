package kitchenpos.application.dto.request;

public class OrderStatusChangeRequest {

    private final String orderStatus;

    private OrderStatusChangeRequest() {
        this(null);
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
