package kitchenpos.ui.request.order;

public class OrderStatusChangeRequest {

    private String orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
