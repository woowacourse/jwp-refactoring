package kitchenpos.application.dto;

public class OrderUpdateRequest {

    private String orderStatus;

    protected OrderUpdateRequest() {
    }

    public OrderUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
