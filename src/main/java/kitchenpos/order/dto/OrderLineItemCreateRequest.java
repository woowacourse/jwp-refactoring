package kitchenpos.order.dto;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private Long quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, Long quantity) {
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
