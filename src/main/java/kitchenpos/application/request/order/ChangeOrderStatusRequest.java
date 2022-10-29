package kitchenpos.application.request.order;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
