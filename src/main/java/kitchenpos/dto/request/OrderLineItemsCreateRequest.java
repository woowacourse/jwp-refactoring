package kitchenpos.dto.request;

public class OrderLineItemsCreateRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemsCreateRequest(final Long menuId, final Long quantity) {
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
