package kitchenpos.dto.order;

public class OrderLineItemRequest {
    private final Long menuId;
    private final long quantity;

    private OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(final Long menuId, final long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
