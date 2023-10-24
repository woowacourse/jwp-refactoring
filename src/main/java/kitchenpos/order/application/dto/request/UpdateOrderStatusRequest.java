package kitchenpos.order.application.dto.request;

public class UpdateOrderStatusRequest {

    private String orderStatus;

    protected UpdateOrderStatusRequest() {
    }

    public UpdateOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
