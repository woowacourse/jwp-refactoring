package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStatusChangeRequest {

    private OrderStatus orderStatus;
}
