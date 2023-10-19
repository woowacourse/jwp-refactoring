package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(orderTable.getId(), orderStatus, orderedTime);
    }

}
