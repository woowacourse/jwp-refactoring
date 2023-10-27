package kitchenpos.application.order.dto;

public class OrderChangeStatusRequest {

    private String orderStatus;

    private OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}