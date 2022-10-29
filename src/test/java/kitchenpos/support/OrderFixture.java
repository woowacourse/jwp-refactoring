package kitchenpos.support;

import static java.time.LocalDateTime.now;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public abstract class OrderFixture {

    public static Order 주문(OrderStatus orderStatus) {
        return new Order(null, orderStatus.name(), now(), null);
    }
}
