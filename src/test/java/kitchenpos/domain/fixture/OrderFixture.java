package kitchenpos.domain.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.List;

public class OrderFixture {

    public static Order 주문_생성(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

}