package kitchenpos.application.dto.response;

public class OrdersStatusResponseDto {

    private Long orderTableId;
    private String orderStatus;

    private OrdersStatusResponseDto() {
    }

    public OrdersStatusResponseDto(Long orderTableId, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
