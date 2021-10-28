package kitchenpos.order.dto;

public class OrderLineItemDto {
    private Long menuId;
    private long quantity;

    public OrderLineItemDto(Long menuId, long quantity) {
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
