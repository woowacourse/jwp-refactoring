package kitchenpos.order.presentation.dto;

import kitchenpos.order.application.dto.OrderLineItemSaveRequest;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private Long quantity;

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemSaveRequest toRequest() {
        return new OrderLineItemSaveRequest(menuId, quantity);
    }
}
