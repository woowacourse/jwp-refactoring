package kitchenpos.fixture;

import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId,
        long quantity) {
        return new OrderLineItem(
            seq,
            orderId,
            menuId,
            quantity
        );
    }

    public static OrderLineItemCreateRequest createOrderLineItemRequest(
        Long menuId,
        long quantity
    ) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }
}
