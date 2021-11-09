package kitchenpos.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusRequest {
    private String orderStatus;

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
