package kitchenpos.fixture;

import static kitchenpos.fixture.OrderLineItemFixture.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static final Order ORDER1 = TestObjectUtils.createOrder(
            1L,
            5L,
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            Collections.singletonList(ORDER_LINE_ITEM1)
    );

    public static final Order ORDER2 = TestObjectUtils.createOrder(
            2L,
            3L,
            OrderStatus.COMPLETION.name(),
            LocalDateTime.now(),
            Collections.singletonList(ORDER_LINE_ITEM2)
    );

    public static final Order ORDER3 = TestObjectUtils.createOrder(
            3L,
            4L,
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            Collections.singletonList(ORDER_LINE_ITEM3)
    );

    public static final Order CHANGING_MEAL_ORDER = TestObjectUtils.createOrder(
            null,
            null,
            OrderStatus.MEAL.name(),
            null,
            null
    );

    public static final List<Order> ORDERS = Collections.singletonList(ORDER1);
}
