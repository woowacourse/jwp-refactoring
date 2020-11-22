package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateInfo {
    private Long menuId;
    private long quantity;

    private OrderLineItemCreateInfo() {
    }

    public OrderLineItemCreateInfo(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
    }
}
