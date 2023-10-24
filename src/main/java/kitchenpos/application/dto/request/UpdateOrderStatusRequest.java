package kitchenpos.application.dto.request;

public class UpdateOrderStatusRequest {
    private String orderStatus;

    private UpdateOrderStatusRequest() {
    }

    public UpdateOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
