package kitchenpos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangeRequest {

    private String orderStatus;

    public String getOrderStatus() {
        return orderStatus;
    }
}
