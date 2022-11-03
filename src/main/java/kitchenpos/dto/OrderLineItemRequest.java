package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.vo.Price;

public class OrderLineItemRequest {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Menu menu) {
        return new OrderLineItem(null, null, menu.getName(), Price.valueOf(menu.getPrice()), quantity);
    }
}
