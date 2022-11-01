package kitchenpos.order.application.dto;

public class OrderChangeRequest {

    private String orderStatus;

    public OrderChangeRequest() {
    }

    public OrderChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
