package kitchenpos.fixture;

import kitchenpos.order.dto.request.OrderLineItemRequest;

public class OrderLineItemFixture {


    public static OrderLineItemRequest generateOrderLineItemRequest(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
