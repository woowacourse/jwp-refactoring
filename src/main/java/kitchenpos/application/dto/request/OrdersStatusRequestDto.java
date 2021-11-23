package kitchenpos.application.dto.request;

public class OrdersStatusRequestDto {

    private Long orderId;
    private String orderStatus;

    public OrdersStatusRequestDto(Long orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
