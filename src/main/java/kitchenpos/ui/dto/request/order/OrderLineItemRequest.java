package kitchenpos.ui.dto.request.order;

import kitchenpos.application.dto.request.order.OrderLineItemRequestDto;

public class OrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemRequestDto toDto() {
        return new OrderLineItemRequestDto(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
