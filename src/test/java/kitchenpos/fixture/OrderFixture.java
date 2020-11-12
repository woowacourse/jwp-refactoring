package kitchenpos.fixture;

import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM_FIXTURE_1;
import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM_FIXTURE_2;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;

public class OrderFixture {

    public static final Order ORDER_FIXTURE_1 = new Order(1L, "COOKING", LocalDateTime.of(2010, 1, 1, 1, 1),
        Arrays.asList(ORDER_LINE_ITEM_FIXTURE_1));
    public static final Order ORDER_FIXTURE_2 = new Order(2L, "MEAL", LocalDateTime.of(2020, 1, 1, 1, 2),
        Arrays.asList(ORDER_LINE_ITEM_FIXTURE_2));
}
