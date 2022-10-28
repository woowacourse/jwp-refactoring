package kitchenpos.application.dto.request;

public class SavedOrderRequest {

    private String orderStatus;

    private SavedOrderRequest() {
    }

    public SavedOrderRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
