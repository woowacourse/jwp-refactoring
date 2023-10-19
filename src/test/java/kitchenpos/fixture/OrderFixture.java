package kitchenpos.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order 주문(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
