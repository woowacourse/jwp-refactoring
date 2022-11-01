package kitchenpos.order.presentation.dto;

import kitchenpos.order.application.dto.request.OrderLineItemCommand;

public record OrderLineItemRequest(Long menuId, long quantity) {

    public OrderLineItemCommand toCommand() {
        return new OrderLineItemCommand(menuId, quantity);
    }
}
