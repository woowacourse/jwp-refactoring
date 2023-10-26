package kitchenpos.order.dto;

public class OrderLineItemDto {

    private final Long menuId;
    private final Integer quantity;

    public OrderLineItemDto(final Long menuId, final Integer quantity) {
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
