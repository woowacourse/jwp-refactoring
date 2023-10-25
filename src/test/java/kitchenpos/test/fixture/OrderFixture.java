package kitchenpos.test.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.vo.OrderStatus;

public class OrderFixture {

    public static Order 주문(
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime
    ) {
        return new Order(orderTable, orderStatus, orderedTime);
    }

    public static Order 주문(
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
