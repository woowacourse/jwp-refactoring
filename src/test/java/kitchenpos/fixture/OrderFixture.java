package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order 주문_생성(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return 주문_생성(orderTableId, OrderStatus.COOKING.name(), orderLineItems);
    }

    public static Order 주문_생성(
            final Long orderTableId,
            final String orderStatus,
            final List<OrderLineItem> orderLineItems
    ) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
