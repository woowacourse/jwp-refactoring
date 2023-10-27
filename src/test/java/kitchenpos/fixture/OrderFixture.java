package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.OrderTable;

public class OrderFixture {

    public static Order 주문(final Long id, final OrderTable orderTable, final OrderStatus orderStatus,
                           final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
