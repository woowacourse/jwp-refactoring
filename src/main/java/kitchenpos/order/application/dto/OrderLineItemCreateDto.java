package kitchenpos.order.application.dto;

public class OrderLineItemCreateDto {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemCreateDto(final Long menuId, final long quantity) {
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
