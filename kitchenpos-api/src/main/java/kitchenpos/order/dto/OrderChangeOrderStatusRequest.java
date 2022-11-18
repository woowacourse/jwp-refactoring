package kitchenpos.order.dto;

public class OrderChangeOrderStatusRequest {

    private String orderStatus;

    private OrderChangeOrderStatusRequest() {
    }

    public OrderChangeOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
