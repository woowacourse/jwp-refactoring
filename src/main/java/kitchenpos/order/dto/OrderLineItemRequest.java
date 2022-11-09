package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemRequest {

    private Long menuId;
    private int quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(final OrderMenu orderMenu) {
        return new OrderLineItem(null, null, orderMenu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
