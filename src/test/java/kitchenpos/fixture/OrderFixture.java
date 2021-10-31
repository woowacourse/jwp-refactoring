package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static Order order() {
        return new Order(0L, 0L, OrderStatus.MEAL.name(), LocalDateTime.now(),
                Arrays.asList(OrderLineItemFixture.orderLineItem()));
    }
}
