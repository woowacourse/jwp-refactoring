package kitchenpos.ui.dto;

import kitchenpos.application.dto.CreateOrderLineItemDto;

public class CreateOrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    protected CreateOrderLineItemRequest() {
    }

    public CreateOrderLineItemRequest(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public CreateOrderLineItemDto toCreateOrderLineItemDto() {
        return new CreateOrderLineItemDto(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}