package kitchenpos.ui.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long menuId;
    private long quantity;

    public OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
