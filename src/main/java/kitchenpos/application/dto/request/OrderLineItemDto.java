package kitchenpos.application.dto.request;

public class OrderLineItemDto {

    private Long menuId;
    private int quantity;

    protected OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
