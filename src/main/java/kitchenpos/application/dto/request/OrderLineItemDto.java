package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long menuId;
    private long quantity;

    public OrderLineItemDto() {
    }

    public OrderLineItemDto(final OrderLineItem orderLineItem) {
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(this.menuId, this.quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
