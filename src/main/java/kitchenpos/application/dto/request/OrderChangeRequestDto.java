package kitchenpos.application.dto.request;

public class OrderChangeRequestDto {

    private Long orderId;
    private String orderStatus;

    private OrderChangeRequestDto() {
    }

    public OrderChangeRequestDto(Long orderId, String orderStatus) {
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
