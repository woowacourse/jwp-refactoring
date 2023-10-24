package kitchenpos.test.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.OrderStatus;

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
