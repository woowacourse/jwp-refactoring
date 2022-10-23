package kitchenpos.application.dto;

import kitchenpos.domain.OrderStatus;
import lombok.Getter;

@Getter
public class UpdateOrderStatusDto {

    private final Long orderId;
    private final OrderStatus orderStatus;

    public UpdateOrderStatusDto(Long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
