package kitchenpos.fixture;

import kitchenpos.order.domain.Order;

import static java.time.LocalDateTime.now;
import static kitchenpos.order.domain.OrderStatus.COOKING;

public class OrderFixture {

    public static Order 주문_생성() {
        Order order = new Order(COOKING, now());
        return order;
    }

}