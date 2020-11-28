package kitchenpos.orderline.application.dto;

import kitchenpos.orderline.model.OrderLineItem;

public class OrderLineCreateRequestDto {
    private Long menuId;
    private long quantity;

    private OrderLineCreateRequestDto() {
    }

    public OrderLineCreateRequestDto(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Long orderId) {
        return new OrderLineItem(null, orderId, menuId, quantity);
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(null, null, menuId, quantity);
    }
}
