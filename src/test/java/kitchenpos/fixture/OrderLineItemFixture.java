package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;

public class OrderLineItemFixture {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, int quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, int quantity) {
        return new OrderLineItem(null, orderId, menuId, quantity);
    }

    public static OrderLineItemCreateRequest createOrderLineItemRequest(Long menuId, int quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }
}
