package kitchenpos.dto;

public class OrderStatusChangeResponse {
    private String orderStatus;

    private OrderStatusChangeResponse() {
    }

    public OrderStatusChangeResponse(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
