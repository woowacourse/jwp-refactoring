package kitchenpos.support.fixture;

import static java.time.LocalDateTime.now;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public abstract class OrderFixture {

    public static Order 주문(OrderStatus orderStatus) {
        return new Order(null, orderStatus, now(), null, null);
    }
}
