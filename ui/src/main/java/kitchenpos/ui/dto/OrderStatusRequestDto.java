package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.UpdateOrderStatusDto;

public class OrderStatusRequestDto {

    private String orderStatus;

    public OrderStatusRequestDto() {
    }

    public UpdateOrderStatusDto toUpdateOrderStatusDto(Long orderId) {
        return new UpdateOrderStatusDto(orderId, orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
