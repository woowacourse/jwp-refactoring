package kitchenpos.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }
}
