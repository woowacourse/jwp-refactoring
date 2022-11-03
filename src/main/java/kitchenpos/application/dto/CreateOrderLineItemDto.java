package kitchenpos.application.dto;

public class CreateOrderLineItemDto {

    private Long menuId;
    private Long quantity;

    private CreateOrderLineItemDto() {
    }

    public CreateOrderLineItemDto(final Long menuId, final Long quantity) {
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
