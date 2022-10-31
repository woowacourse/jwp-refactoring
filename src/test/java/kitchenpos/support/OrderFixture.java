package kitchenpos.support;

import static java.time.LocalDateTime.now;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public abstract class OrderFixture {

    public static Order 주문(OrderStatus orderStatus) {
        return new Order(null, orderStatus, now(), null, null);
    }
}
