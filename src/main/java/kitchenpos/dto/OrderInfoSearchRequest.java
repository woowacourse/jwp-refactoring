package kitchenpos.dto;

public class OrderInfoSearchRequest {

    private Long orderId;

    public OrderInfoSearchRequest(Long orderId) {
        this.orderId = orderId;
    }

    public OrderInfoSearchRequest() {
    }

    public Long getOrderId() {
        return orderId;
    }
}
