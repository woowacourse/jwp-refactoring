package kitchenpos.order.presentation.dto;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private Long quantity;

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(final Long menuId, final Long quantity) {
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
