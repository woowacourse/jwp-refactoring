package kitchenpos.application.dto;

public class OrderStatusChangeRequest {

    private final String orderStatus;

    public OrderStatusChangeRequest() {
        this(null);
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
