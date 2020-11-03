package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangeRequest {

    private OrderStatus orderStatus;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
