package kitchenpos.domain.order.service.dto;

public class OrderLineItemCreateRequest {

    private final Long menuId;
    private final Long quantity;

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
