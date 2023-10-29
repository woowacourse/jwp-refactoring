package kitchenpos.dto.request;

public class ChangeOrderRequest {

    private String orderStatus;

    private ChangeOrderRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
