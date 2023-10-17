package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long menuId;
    private final long quantity;

    private OrderLineItemResponse(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
