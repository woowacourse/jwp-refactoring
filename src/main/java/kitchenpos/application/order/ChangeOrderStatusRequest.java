package kitchenpos.application.order;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    private ChangeOrderStatusRequest() {
    }

    ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
