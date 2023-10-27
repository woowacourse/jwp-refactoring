package kitchenpos.application.dto;

public class OrderChangeOrderStatusRequest {

    private String orderStatus;

    public OrderChangeOrderStatusRequest() {
    }

    public OrderChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
