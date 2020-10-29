package kitchenpos.dto.order;

public class OrderStatusRequest {
    private String orderStatus;

    private OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
