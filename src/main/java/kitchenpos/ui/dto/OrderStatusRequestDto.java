package kitchenpos.ui.dto;

import kitchenpos.application.dto.UpdateOrderStatusDto;
import kitchenpos.domain.OrderStatus;

public class OrderStatusRequestDto {

    private String orderStatus;

    public OrderStatusRequestDto() {
    }

    public UpdateOrderStatusDto toUpdateOrderStatusDto(Long orderId) {
        return new UpdateOrderStatusDto(orderId, OrderStatus.valueOf(orderStatus));
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
