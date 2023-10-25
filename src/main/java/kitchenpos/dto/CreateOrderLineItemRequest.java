package kitchenpos.dto;

public class CreateOrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public CreateOrderLineItemRequest() {
    }

    public CreateOrderLineItemRequest(final Long menuId, final long quantity) {
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
