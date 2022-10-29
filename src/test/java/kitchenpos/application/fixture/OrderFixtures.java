package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixtures {

    public static final Order generateOrder(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return generateOrder(null, orderTableId, null, LocalDateTime.now(), orderLineItems);
    }

    public static final Order generateOrder(final Long orderTableId, final OrderStatus orderStatus,
                                            final List<OrderLineItem> orderLineItems) {
        return generateOrder(null, orderTableId, orderStatus.name(), LocalDateTime.now(), orderLineItems);
    }

    public static final Order generateOrder(final Long id, final Order order) {
        return generateOrder(
                id,
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems()
        );
    }

    public static final Order generateOrder(final Long id,
                                            final Long orderTableId,
                                            final String orderStatus,
                                            final LocalDateTime orderedTime,
                                            final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
