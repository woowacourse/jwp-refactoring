package kitchenpos.dto.request;

public class OrderUpdateRequest {

    private final String orderStatus;

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
