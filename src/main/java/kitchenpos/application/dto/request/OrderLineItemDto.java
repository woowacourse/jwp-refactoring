package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemDto() {
    }

    public OrderLineItemDto(final OrderLineItem orderLineItem) {
        this.orderId = orderLineItem.getOrderId();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
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
        return new OrderLineItem(null, this.orderId, this.menuId, this.quantity);
    }
}
