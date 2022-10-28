package kitchenpos.ui.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }
}
