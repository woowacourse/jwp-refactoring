package kitchenpos.application.dto.request;

public class OrderLineItemRequest {

    private long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final long menuId, final long quantity) {
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
