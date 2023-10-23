package kitchenpos.application.dto;

public class OrderLineItemDto {

    private long menuId;

    private long quantity;

    public OrderLineItemDto(final long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
