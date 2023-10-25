package kitchenpos.dto;

public class OrderLineItemInOrderDto {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemInOrderDto(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
