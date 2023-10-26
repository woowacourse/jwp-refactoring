package kitchenpos.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long menuId;
    private long quantity;

    protected OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getMenuId(), orderLineItem.getQuantity().getValue());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}