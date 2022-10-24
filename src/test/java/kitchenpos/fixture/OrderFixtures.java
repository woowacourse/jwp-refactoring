package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrderFixtures {

    public static Order createOrder(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order createOrder(final Long orderTableId, final OrderStatus orderStatus,
                                    final List<OrderLineItem> orderLineItems) {
        Order order = createOrder(orderTableId, orderLineItems);
        order.setOrderStatus(orderStatus.name());
        return order;
    }
}
