package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    private static final Order order = new Order(0L, 0L, OrderStatus.MEAL.name(), LocalDateTime.now(),
            Arrays.asList(OrderLineItemFixture.orderLineItem()));

    public static Order order() {
        return order;
    }
}
