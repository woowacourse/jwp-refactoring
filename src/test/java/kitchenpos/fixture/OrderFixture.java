package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(orderTable, orderStatus, orderedTime);
    }

}
