package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderLineItem;

public record OrderLineItemCommand(Long menuId, long quantity) {

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
