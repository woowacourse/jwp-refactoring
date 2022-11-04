package kitchenpos.order.application.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemRequest(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(final Menu menu) {
        return new OrderLineItem(menu.getName(), menu.getPrice(), quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
