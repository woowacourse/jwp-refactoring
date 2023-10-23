package kitchenpos.test.fixtures;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.vo.OrderStatus;

public enum OrderFixtures {
    EMPTY(0L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.emptyList()),
    BASIC(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of(OrderLineItemFixtures.BASIC.get()));

    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    OrderFixtures(
            final Long orderTableId,
            final String orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order get() {
        final Order order = new Order();
        order.setOrderTable(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
