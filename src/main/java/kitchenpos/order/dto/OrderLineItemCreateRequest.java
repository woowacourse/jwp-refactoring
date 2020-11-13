package kitchenpos.order.dto;

public class OrderLineItemCreateRequest {
    private Long quantity;
    private Long menuId;

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long quantity, Long menuId) {
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }
}