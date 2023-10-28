package kitchenpos.domain.fixture;

import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderLineItems;
import kitchenpos.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public abstract class OrderFixture {

    private OrderFixture() {
    }

    public static Order order(final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        return order(null, orderStatus, orderedTime, orderLineItems);
    }

    public static Order order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> toAddOrderLineItems) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        toAddOrderLineItems.forEach(orderLineItems::add);
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
