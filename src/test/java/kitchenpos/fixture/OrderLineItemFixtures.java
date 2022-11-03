package kitchenpos.fixture;

import kitchenpos.order.application.dto.request.OrderLineItemCreateRequest;

public class OrderLineItemFixtures {

    public static OrderLineItemCreateRequest createOrderLineItem(final Long menuId, final long quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }
}
