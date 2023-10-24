package kitchenpos.dto;

public class OrderStatusChangeRequest {
    private String orderStatus;

    protected OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
