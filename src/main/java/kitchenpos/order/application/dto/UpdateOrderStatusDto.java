package kitchenpos.order.application.dto;

public class UpdateOrderStatusDto {

    private Long orderId;
    private String orderStatus;

    public UpdateOrderStatusDto() {
    }

    public UpdateOrderStatusDto(Long orderId, String orderStatus) {
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
