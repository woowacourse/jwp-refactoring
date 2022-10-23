package kitchenpos.ui.dto;

import kitchenpos.application.dto.UpdateOrderStatusDto;
import kitchenpos.domain.OrderStatus;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class OrderStatusRequestDto {

    private String orderStatus;

    public UpdateOrderStatusDto toUpdateOrderStatusDto(Long orderId) {
        return new UpdateOrderStatusDto(orderId, OrderStatus.valueOf(orderStatus));
    }
}
