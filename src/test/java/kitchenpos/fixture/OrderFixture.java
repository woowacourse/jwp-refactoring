package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order newOrder(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime localDateTime,
                                  final OrderLineItem... orderLineItems) {
        final var order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(localDateTime);
        order.setOrderLineItems(List.of(orderLineItems));
        return order;
    }

    public static Order newOrder(final Long orderTableId, final OrderStatus orderStatus, final OrderLineItem... orderLineItems) {
        return newOrder(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }
}
