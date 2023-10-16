package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

public final class OrderFactory {

    private OrderFactory() {
    }

    public static Order createOrderOf(final Long orderTableId, final OrderLineItem... orderLineItems) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(new ArrayList<>());
        order.getOrderLineItems().addAll(List.of(orderLineItems));
        return order;
    }
}
