package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.vo.Price;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

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

    public OrderLineItem toEntity(final Menu menu) {
        return new OrderLineItem(null, menu.getName(), Price.valueOf(menu.getPrice()), quantity);
    }
}
