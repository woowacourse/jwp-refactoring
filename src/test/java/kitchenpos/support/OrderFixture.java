package kitchenpos.support;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order 주문_생성(final Long orderTableId, final OrderLineItem... orderLineItems){
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(List.of(orderLineItems));
        return order;
    }
}
