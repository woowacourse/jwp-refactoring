package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order 주문(final Long orderTableId, final OrderStatus orderStatus, final OrderLineItem... items) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(Arrays.asList(items));
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    public static Order 주문(final Long orderTableId, final OrderLineItem... items) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(Arrays.asList(items));
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }
}
