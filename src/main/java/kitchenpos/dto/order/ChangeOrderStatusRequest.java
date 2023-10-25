package kitchenpos.dto.order;

public class ChangeOrderStatusRequest {

    private final String orderStatus;

    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
