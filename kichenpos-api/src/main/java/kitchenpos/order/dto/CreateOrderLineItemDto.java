package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class CreateOrderLineItemDto {

    private final Long menuId;
    private final long quantity;

    public CreateOrderLineItemDto(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
    }
}
