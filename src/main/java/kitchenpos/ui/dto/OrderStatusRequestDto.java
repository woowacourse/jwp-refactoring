package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.UpdateOrderStatusDto;
import kitchenpos.common.domain.order.OrderStatus;

public class OrderStatusRequestDto {

    private String orderStatus;

    public OrderStatusRequestDto() {
    }

    public UpdateOrderStatusDto toUpdateOrderStatusDto(Long orderId) {
        return new UpdateOrderStatusDto(orderId, OrderStatus.valueOf(orderStatus));
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
