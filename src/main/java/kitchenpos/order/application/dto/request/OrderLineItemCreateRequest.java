package kitchenpos.order.application.dto.request;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(final Menu menu) {
        return new OrderLineItem(menuId, quantity, menu.getName(), menu.getPrice());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
