package kitchenpos.order.application.dto.request;

import kitchenpos.order.domain.OrderLineItem;

public record OrderLineItemCommand(Long menuId, long quantity) {

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
