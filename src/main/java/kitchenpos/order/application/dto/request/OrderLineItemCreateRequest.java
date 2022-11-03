package kitchenpos.order.application.dto.request;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
