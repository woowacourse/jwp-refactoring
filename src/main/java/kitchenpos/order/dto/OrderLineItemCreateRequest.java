package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemCreateRequest of(final Long menuId, final OrderLineItem orderLineItem) {
        return new OrderLineItemCreateRequest(menuId, orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
