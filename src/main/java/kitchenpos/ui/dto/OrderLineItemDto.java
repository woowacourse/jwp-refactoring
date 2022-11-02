package kitchenpos.ui.dto;

public class OrderLineItemDto {

    private Long menuId;
    private Long quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, Long quantity) {
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
