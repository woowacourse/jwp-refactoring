package kitchenpos.ui.dto;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    protected ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
