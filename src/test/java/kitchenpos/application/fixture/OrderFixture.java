package kitchenpos.application.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public abstract class OrderFixture {

    private OrderFixture() {
    }

    public static Order order(final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        return order(null, orderStatus, orderedTime, orderLineItems);
    }

    public static Order order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> toAddOrderLineItems) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        toAddOrderLineItems.forEach(orderLineItems::add);
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
