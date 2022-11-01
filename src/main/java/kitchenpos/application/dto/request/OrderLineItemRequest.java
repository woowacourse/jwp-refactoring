package kitchenpos.application.dto.request;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
    }
}
