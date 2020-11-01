package kitchenpos.ui.dto;

public class OrderChangeStatusRequest {

    private String orderStatus;

    private OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String toString() {
        return "OrderChangeStatusRequest{" +
            "orderStatus='" + orderStatus + '\'' +
            '}';
    }
}
