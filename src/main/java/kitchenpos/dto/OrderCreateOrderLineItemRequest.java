package kitchenpos.dto;

public class OrderCreateOrderLineItemRequest {

    private long menuId;
    private long quantity;

    public OrderCreateOrderLineItemRequest() {
    }

    public OrderCreateOrderLineItemRequest(final long menuId, final long quantity) {
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
