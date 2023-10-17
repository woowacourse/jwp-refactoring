package kitchenpos.ui.dto.order;

public class UpdateOrderRequest {

    private final String orderStatus;

    public UpdateOrderRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
