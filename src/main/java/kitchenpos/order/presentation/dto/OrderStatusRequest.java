package kitchenpos.order.presentation.dto;

import kitchenpos.order.application.dto.request.OrderStatusCommand;

public record OrderStatusRequest(String orderStatus) {

    public OrderStatusCommand toCommand() {
        return new OrderStatusCommand(orderStatus);
    }
}
