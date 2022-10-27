package kitchenpos.fixture;

import kitchenpos.dto.OrderLineItemCreateRequest;

public class OrderLineItemFixtures {

    public static OrderLineItemCreateRequest createOrderLineItem(final Long menuId, final long quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }
}
