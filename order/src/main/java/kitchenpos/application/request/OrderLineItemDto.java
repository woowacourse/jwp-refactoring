package kitchenpos.application.request;

public class OrderLineItemDto {
    private final Long menuId;
    private final Long quantity;

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
