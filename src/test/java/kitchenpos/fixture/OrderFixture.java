package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public final class OrderFixture {

    public static Order 주문_생성(final Long orderTableId) {
        final Order order = new Order();

        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(Collections.emptyList());
        order.setOrderStatus("COMPLETION");
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    public static Order 주문_생성(final String orderStatus) {
        final Order order = new Order();

        order.setOrderStatus(orderStatus);

        return order;
    }

    public static Order 주문_생성(final Long orderTableId, final String orderStatus) {
        final Order order = new Order();

        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(Collections.emptyList());
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    public static Order 주문_생성(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        final Order order = new Order();

        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static Order 주문_생성(
            final Long orderTableId,
            final List<OrderLineItem> orderLineItems,
            final String orderStatus
    ) {
        final Order order = new Order();

        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    private OrderFixture() {
    }
}
