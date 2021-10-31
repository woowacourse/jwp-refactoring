package kitchenpos.ui.dto;

public class OrderStatusResponse {
    private String orderStatus;

    private OrderStatusResponse() {
    }

    private OrderStatusResponse(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusResponse from(String orderStatus) {
        return new OrderStatusResponse(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
