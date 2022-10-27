package kitchenpos.dto.request;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
