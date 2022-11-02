package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order create(final OrderTable orderTable, final OrderStatus orderStatus, final OrderLineItem... orderLineItems) {
        return new Order(null, orderTable, orderStatus, LocalDateTime.now(), Arrays.asList(orderLineItems));
    }
}
