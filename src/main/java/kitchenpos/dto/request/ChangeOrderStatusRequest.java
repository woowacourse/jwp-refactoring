package kitchenpos.dto.request;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    private ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
