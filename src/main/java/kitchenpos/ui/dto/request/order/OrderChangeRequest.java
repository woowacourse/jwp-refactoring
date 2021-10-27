package kitchenpos.ui.dto.request.order;

public class OrderChangeRequest {

    private String orderStatus;

    private OrderChangeRequest() {
    }

    public OrderChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
