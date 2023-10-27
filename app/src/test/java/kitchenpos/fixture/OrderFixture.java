package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order of(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(orderTableId, orderStatus, orderedTime);
    }
}
