package kitchenpos.order.service.dto;

public class OrderUpateRequest {

    private final String orderStatus;

    public OrderUpateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
