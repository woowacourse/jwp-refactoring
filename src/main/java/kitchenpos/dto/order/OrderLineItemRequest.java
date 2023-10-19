package kitchenpos.dto.order;

public class OrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    private OrderLineItemRequest(final Long menuId,
                                 final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(final Long menuId,
                                          final Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
