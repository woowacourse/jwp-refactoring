package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;

public class OrderFixture {
    public static Order order() {
        return new Order(0L, 0L, OrderStatus.MEAL.name(), LocalDateTime.now(),
                Arrays.asList(OrderLineItemFixture.orderLineItem()));
    }

    public static OrderRequest orderRequest() {
        return new OrderRequest(0L, Arrays.asList(OrderLineItemFixture.orderLineItem()));
    }
}
