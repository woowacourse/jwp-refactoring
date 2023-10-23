package kitchenpos.ui.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private Integer quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
