package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order 주문(final Long id, final Long orderTableId, final String orderStatus,
                           final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}
