package kitchenpos.ui.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.application.order.dto.ChangeOrderStatusCommand;
import kitchenpos.domain.order.OrderStatus;

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
