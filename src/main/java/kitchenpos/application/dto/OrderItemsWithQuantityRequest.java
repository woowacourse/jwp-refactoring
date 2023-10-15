package kitchenpos.application.dto;

public class OrderItemsWithQuantityRequest {
    private final Long menuId;
    private final Long quantity;

    public OrderItemsWithQuantityRequest(final Long menuId, final Long quantity) {
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
