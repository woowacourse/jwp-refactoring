package kitchenpos.order.application.dto;

public class OrderLineItemDto {

    final Long menuId;
    final Long quantity;

    public OrderLineItemDto(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
