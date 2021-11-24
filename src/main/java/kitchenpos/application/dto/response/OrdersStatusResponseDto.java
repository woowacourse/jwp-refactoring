package kitchenpos.application.dto.response;

public class OrdersStatusResponseDto {

    private String orderStatus;

    private OrdersStatusResponseDto() {
    }

    public OrdersStatusResponseDto(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
