package kitchenpos.fixtures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public enum OrderFixtures {

    COOKING_ORDER(null, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()),
    MEAL_ORDER(null, OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>()),
    COMPLETION_ORDER(null, OrderStatus.COMPLETION.name(), LocalDateTime.now(), new ArrayList<>());

    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    OrderFixtures(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                  final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order create() {
        return new Order(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order createWithOrderLineItems(final OrderLineItem... orderLineItems) {
        return new Order(null, orderTableId, orderStatus, orderedTime, Arrays.asList(orderLineItems));
    }

    public Order createWithOrderTableIdAndOrderLineItems(final Long orderTableId,
                                                         final OrderLineItem... orderLineItems) {
        return new Order(null, orderTableId, orderStatus, orderedTime, Arrays.asList(orderLineItems));
    }
}

