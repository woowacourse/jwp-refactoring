package kitchenpos.order.application.dto;

public class UpdateOrderStatusRequestDto {

    private String orderStatus;

    public UpdateOrderStatusRequestDto(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
