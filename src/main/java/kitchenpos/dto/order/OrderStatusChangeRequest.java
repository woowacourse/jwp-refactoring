package kitchenpos.dto.order;

public class OrderStatusChangeRequest {

    private String orderStatus;

    private OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
