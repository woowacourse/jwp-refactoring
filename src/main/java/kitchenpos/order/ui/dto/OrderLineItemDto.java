package kitchenpos.order.ui.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long menuId;
    private long quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(final Menu menu) {
        return new OrderLineItem(menuId,quantity, menu.getName(), menu.getPrice());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
