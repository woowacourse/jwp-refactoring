package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static final OrderLineItem CHICKEN_SET = new OrderLineItem(1L, 1L, 2);

    public static final Order ORDER = new Order(
            1L,
            1L,
            OrderStatus.COOKING.name(),
            LocalDateTime.of(2023, 10, 12, 0, 12, 5),
            List.of(CHICKEN_SET)
    );

    public static final Order ORDER_NON_ID = new Order(
            null,
            1L,
            OrderStatus.COOKING.name(),
            LocalDateTime.of(2023, 10, 12, 0, 12, 5),
            List.of(CHICKEN_SET)
    );
}
