package kitchenpos.application.dto;

public class OrderStatusChangeRequest {

    private String orderStatus;

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    protected OrderStatusChangeRequest() {
    }
}
