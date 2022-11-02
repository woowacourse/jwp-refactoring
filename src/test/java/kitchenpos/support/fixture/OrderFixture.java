package kitchenpos.support.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    public static Order create(final OrderTable orderTable, final OrderStatus orderStatus, final OrderLineItem... orderLineItems) {
        return new Order(null, orderTable, orderStatus, LocalDateTime.now(), Arrays.asList(orderLineItems));
    }
}
