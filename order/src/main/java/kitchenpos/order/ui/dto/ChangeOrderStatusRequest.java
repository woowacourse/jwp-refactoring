package kitchenpos.order.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.application.dto.ChangeOrderStatusCommand;
import kitchenpos.order.domain.OrderStatus;

public class ChangeOrderStatusRequest {

    @JsonProperty("orderStatus")
    private String orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ChangeOrderStatusCommand toCommand(Long orderId) {
        return new ChangeOrderStatusCommand(orderId, OrderStatus.valueOf(orderStatus));
    }
}
