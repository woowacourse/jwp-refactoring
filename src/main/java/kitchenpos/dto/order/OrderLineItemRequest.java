package kitchenpos.dto.order;

public class OrderLineItemRequest {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
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
