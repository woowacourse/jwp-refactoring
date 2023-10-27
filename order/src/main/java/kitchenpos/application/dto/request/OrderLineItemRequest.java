package kitchenpos.application.dto.request;

public class OrderLineItemRequest {

    private Long menuId;
    private int quantity;

    protected OrderLineItemRequest() {
    }

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
