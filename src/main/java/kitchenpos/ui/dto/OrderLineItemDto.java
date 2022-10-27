package kitchenpos.ui.dto;

public class OrderLineItemDto {

    private Long menuId;
    private Integer quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, Integer quantity) {
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
