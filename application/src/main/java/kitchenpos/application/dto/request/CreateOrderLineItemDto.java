package kitchenpos.application.dto.request;

public class CreateOrderLineItemDto {

    private final Long menuId;
    private final Integer quantity;

    public CreateOrderLineItemDto(Long menuId, Integer quantity) {
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
