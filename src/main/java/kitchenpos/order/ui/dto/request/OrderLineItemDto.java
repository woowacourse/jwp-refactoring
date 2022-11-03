package kitchenpos.order.ui.dto.request;

public class OrderLineItemDto {

    private Long menuId;
    private long quantity;

    public OrderLineItemDto() {
    }

    public OrderLineItemDto(final Long menuId, final long quantity) {
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
