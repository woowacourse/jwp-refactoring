package kitchenpos.support.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixture {

    public static Order create(final Long orderTableId, final OrderStatus orderStatus, final OrderLineItem... orderLineItems) {
        return new Order(null, orderTableId, orderStatus, LocalDateTime.now(), Arrays.asList(orderLineItems));
    }
}
