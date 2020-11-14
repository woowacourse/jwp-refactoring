package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderLineItemCreateRequest;

public class OrderLineItemFixture {

    public static final int DEFAULT_QUANTITY = 1;

    public static OrderLineItemCreateRequest createRequest(Long menuId, int quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }

    public static OrderLineItem createWithoutId(Long menuId, Long orderId) {
        return new OrderLineItem(orderId, menuId, DEFAULT_QUANTITY);
    }
}
