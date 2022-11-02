package kitchenpos.order.dto.application;

public class OrderLineItemDto {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemDto(Long menuId, long quantity) {
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
