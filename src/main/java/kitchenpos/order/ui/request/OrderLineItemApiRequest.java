package kitchenpos.order.ui.request;

import kitchenpos.order.application.request.OrderLineItemRequest;

public class OrderLineItemApiRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemApiRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemRequest toServiceRequest() {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
