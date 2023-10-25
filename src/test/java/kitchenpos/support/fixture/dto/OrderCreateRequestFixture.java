package kitchenpos.support.fixture.dto;

import java.util.List;
import kitchenpos.application.dto.order.OrderCreateRequest;
import kitchenpos.application.dto.order.OrderLineItemRequest;

public class OrderCreateRequestFixture {

    public static OrderCreateRequest orderCreateRequest(final Long orderTableId,
                                                        final List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTableId, orderLineItems);
    }

    public static OrderCreateRequest orderCreateRequest(final Long orderTableId,
                                                        final Long orderLineItemsMenuId,
                                                        final int quantity) {
        return new OrderCreateRequest(orderTableId, List.of(new OrderLineItemRequest(orderLineItemsMenuId, quantity)));
    }
}
