package kitchenpos.support.fixture.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order getOrder(final Long orderTableId, final OrderStatus orderStatus) {
        final Order order = new Order(orderTableId, LocalDateTime.now());
        order.changeStatus(orderStatus);
        return order;
    }

    public static Order getOrder(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        final Order order = new Order(orderTableId, LocalDateTime.now());
        order.registerOrderLineItems(new OrderLineItems(orderLineItems));
        return order;
    }
}
