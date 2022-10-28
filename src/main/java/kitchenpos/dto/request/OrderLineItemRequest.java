package kitchenpos.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private int quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(null, null, menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
