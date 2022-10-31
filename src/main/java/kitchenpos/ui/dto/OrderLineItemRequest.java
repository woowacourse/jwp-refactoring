package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.OrderLineItemCommand;

public record OrderLineItemRequest(Long menuId, long quantity) {

    public OrderLineItemCommand toCommand() {
        return new OrderLineItemCommand(menuId, quantity);
    }
}
