package kitchenpos.application.dto.order;

public class OrderStatusChangeRequest {

    private String orderStatus;

    private OrderStatusChangeRequest() {}

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
