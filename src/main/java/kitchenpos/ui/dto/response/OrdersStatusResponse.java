package kitchenpos.ui.dto.response;

public class OrdersStatusResponse {

    private String orderStatus;

    private OrdersStatusResponse() {
    }

    public OrdersStatusResponse(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
