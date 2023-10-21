package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order createOrder(final Long id,
                                    final Long orderTableId,
                                   final List<OrderLineItem> lineItems) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(lineItems);
        order.setOrderStatus(OrderStatus.COOKING.name());

        return order;
    }

    public static Order createOrder(final Long id, final Long orderTableId) {
        return createOrder(id, orderTableId, Collections.emptyList());
    }
}
