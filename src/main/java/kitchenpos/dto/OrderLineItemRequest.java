package kitchenpos.dto;

public class OrderLineItemRequest {

    private Long menuId;
    private int quantity;

    public OrderLineItemRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
