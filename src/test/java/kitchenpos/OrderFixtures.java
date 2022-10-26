package kitchenpos;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixtures {

    private OrderFixtures() {
    }

    public static Order createOrder() {
        return new Order(1L, null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));
    }
}
