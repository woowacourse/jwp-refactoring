package kitchenpos.ui.dto.order;

public class OrderStatusChangeRequest {

    private String orderStatus;

    OrderStatusChangeRequest() {

    }

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
