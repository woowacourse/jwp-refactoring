package kitchenpos.ui.dto.request;

public class UpdateOrderStateRequest {

    private String orderState;

    public UpdateOrderStateRequest() {
    }

    public UpdateOrderStateRequest(final String orderState) {
        this.orderState = orderState;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(final String orderState) {
        this.orderState = orderState;
    }
}
