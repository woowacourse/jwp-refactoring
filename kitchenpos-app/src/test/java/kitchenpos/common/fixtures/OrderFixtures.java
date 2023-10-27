package kitchenpos.common.fixtures;

import java.util.List;
import kitchenpos.application.order.dto.OrderChangeStatusRequest;
import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.application.order.dto.OrderLineItemRequest;
import kitchenpos.domain.order.OrderStatus;

public class OrderFixtures {

    /**
     * ORDER_TABLE_ID
     */
    public static final Long ORDER1_ORDER_TABLE_ID = 1L;

    /**
     * ORDER_LINE_ITEMS_REQUEST
     */
    public static List<OrderLineItemRequest> ORDER1_ORDER_LINE_ITEMS_REQUEST() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        return List.of(orderLineItemRequest);
    }

    /**
     * CREATE_REQUEST
     */
    public static OrderCreateRequest ORDER1_CREATE_REQUEST() {
        return new OrderCreateRequest(ORDER1_ORDER_TABLE_ID, ORDER1_ORDER_LINE_ITEMS_REQUEST());
    }

    /**
     * CHANGE_STATUS_REQUEST
     */
    public static OrderChangeStatusRequest ORDER1_CHANGE_STATUS_REQUEST() {
        return new OrderChangeStatusRequest(OrderStatus.MEAL.name());
    }
}
