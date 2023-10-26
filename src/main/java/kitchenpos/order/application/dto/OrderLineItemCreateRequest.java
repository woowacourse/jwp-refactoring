package kitchenpos.order.application.dto;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
