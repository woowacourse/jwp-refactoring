package ordertable.main.java.kitchenpos.order.dto;

public class OrderStatusChangeRequest {
    private String orderStatus;

    protected OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
