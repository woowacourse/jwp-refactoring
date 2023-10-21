package kitchenpos.dto;

public class OrderLineItemCreateRequest {

    private final Long menuId;
    private final Long quantity;

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
