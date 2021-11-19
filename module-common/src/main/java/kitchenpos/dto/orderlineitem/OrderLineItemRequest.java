package kitchenpos.dto.orderlineitem;

public class OrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemRequest() {
        this(null, null);
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
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
