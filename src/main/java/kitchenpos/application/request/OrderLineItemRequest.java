package kitchenpos.application.request;

public class OrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final Long quantity) {
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
