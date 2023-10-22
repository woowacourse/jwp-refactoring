package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, long quantity) {
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
        return OrderLineItem.of(menuId, quantity);
    }
}
