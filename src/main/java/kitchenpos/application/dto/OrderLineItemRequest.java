package kitchenpos.application.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId; // 애그리거트 분리를 위해 id 참조
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
