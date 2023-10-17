package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateDto {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemCreateDto(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toDomain() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
