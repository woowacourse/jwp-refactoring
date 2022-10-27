package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.OrderStatusCommand;

public record OrderStatusRequest(String orderStatus) {

    public OrderStatusCommand toCommand() {
        return new OrderStatusCommand(orderStatus);
    }
}
