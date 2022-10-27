package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
        this(null, null, 0);
    }

    public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
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
