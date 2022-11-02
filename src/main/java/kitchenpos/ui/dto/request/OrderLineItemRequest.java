package kitchenpos.ui.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this(null, menuId, quantity);

    }

    public OrderLineItemRequest(final Long orderId, final Long menuId, final long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }


    public Long getOrderId() {
        return orderId;
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
