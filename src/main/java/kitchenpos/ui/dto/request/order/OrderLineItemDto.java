package kitchenpos.ui.dto.request.order;

public class OrderLineItemDto {

    private Long menuId;
    private Long quantity;

    private OrderLineItemDto() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
