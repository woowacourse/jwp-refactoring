package kitchenpos.ui.dto.order;

public class ChangeOrderStatusRequest {

    private final String orderStatus;

    public ChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
