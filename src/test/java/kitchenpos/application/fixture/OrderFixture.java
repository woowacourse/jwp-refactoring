package kitchenpos.application.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public abstract class OrderFixture {

    private OrderFixture() {
    }

    public static Order order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
