package kitchenpos.supports;

import java.util.List;
import kitchenpos.order.service.dto.OrderLineItemRequest;
import kitchenpos.order.service.dto.OrderRequest;
import kitchenpos.order.service.dto.OrderStatusRequest;

public class OrderFixture {

    private static final long DEFAULT_QUANTITY = 2;

    public static OrderRequest of(final Long menuId, final Long orderTableId) {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, DEFAULT_QUANTITY);
        return new OrderRequest(orderTableId, List.of(orderLineItemRequest));
    }

    public static OrderStatusRequest createMeal() {
        return new OrderStatusRequest("MEAL");
    }

    public static OrderStatusRequest createCompletion() {
        return new OrderStatusRequest("COMPLETION");
    }
}
