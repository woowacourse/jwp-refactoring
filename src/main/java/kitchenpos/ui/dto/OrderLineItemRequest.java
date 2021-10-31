package kitchenpos.ui.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    private OrderLineItemRequest() {
    }

    private OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
