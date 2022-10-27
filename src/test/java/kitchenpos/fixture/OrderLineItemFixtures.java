package kitchenpos.fixture;

import kitchenpos.dto.request.OrderLineItemCreateRequest;

public class OrderLineItemFixtures {

    public static OrderLineItemCreateRequest createOrderLineItem(final Long menuId, final long quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }
}
