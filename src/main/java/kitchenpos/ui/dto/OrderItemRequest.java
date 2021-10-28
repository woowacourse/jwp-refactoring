package kitchenpos.ui.dto;

public class OrderItemRequest {
    private Long menuId;
    private Long quantity;

    private OrderItemRequest() {
    }

    private OrderItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderItemRequest of(Long menuId, Long quantity) {
        return new OrderItemRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
