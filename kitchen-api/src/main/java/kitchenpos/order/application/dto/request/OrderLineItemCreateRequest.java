package kitchenpos.order.application.dto.request;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
