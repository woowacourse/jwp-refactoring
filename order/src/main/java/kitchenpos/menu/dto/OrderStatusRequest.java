package kitchenpos.menu.dto;

public class OrderStatusRequest {

    private String orderStatus;

    protected OrderStatusRequest() {
    }

    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
