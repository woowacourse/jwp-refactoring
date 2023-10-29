package kitchenpos.order.dto;

public class OrderLineRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineRequest(final Long menuId, final Long quantity) {
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
