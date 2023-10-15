package kitchenpos.support.fixture.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order getOrder(final Long orderTableId, final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    public static Order getOrder(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
