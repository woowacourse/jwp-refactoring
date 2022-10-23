package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private Integer quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Long orderId) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
