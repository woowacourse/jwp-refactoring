package kitchenpos.ui.request;

public class UpdateOrderStatusRequest {

    private String orderStatus;

    public UpdateOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
