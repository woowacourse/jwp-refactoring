package kitchenpos.order.domain.dto.request;

public class OrderStatusChangeRequest {

    private String orderStatus;

    protected OrderStatusChangeRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
