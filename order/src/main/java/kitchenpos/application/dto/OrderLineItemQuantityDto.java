package kitchenpos.application.dto;

public class OrderLineItemQuantityDto {

    private Long menuId;
    private long quantity;

    public OrderLineItemQuantityDto(Long menuId, long quantity) {
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
