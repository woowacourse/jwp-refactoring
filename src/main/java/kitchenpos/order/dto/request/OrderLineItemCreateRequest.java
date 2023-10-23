package kitchenpos.order.dto.request;

public class OrderLineItemCreateRequest {
    private final long menuId;
    private final long quantity;

    public OrderLineItemCreateRequest(final long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity;
    }
}
