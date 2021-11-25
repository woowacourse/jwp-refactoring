package kitchenpos.order.ui.dto.request;

public class OrderLineItemRequestDto {

    private Long menuId;
    private Long quantity;

    private OrderLineItemRequestDto() {
    }

    public OrderLineItemRequestDto(Long menuId, Long quantity) {
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
