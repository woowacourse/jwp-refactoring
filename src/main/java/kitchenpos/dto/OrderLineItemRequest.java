package kitchenpos.dto;

public class OrderLineItemRequest {

    private Long menuId;
    private Integer quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Integer quantity) {
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
