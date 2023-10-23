package kitchenpos.ui.dto.request;

public class UpdateOrderStatusRequest {

    private String orderStatus;

    public UpdateOrderStatusRequest() {
    }

    public UpdateOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
