package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.OrderStatus.COOKING;

public class OrderFixture {

    public static Order 주문_생성(OrderTable orderTable) {
        Order order = new Order(orderTable, COOKING, now());
        return order;
    }

}