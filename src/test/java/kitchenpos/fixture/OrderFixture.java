package kitchenpos.fixture;

import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM_FIXTURE_1;
import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM_FIXTURE_2;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;

public class OrderFixture {

    public static final Order ORDER_FIXTURE_1 = new Order();
    public static final Order ORDER_FIXTURE_2 = new Order();

    static {
        ORDER_FIXTURE_1.setOrderTableId(1L);
        ORDER_FIXTURE_1.setOrderStatus("COOKING");
        ORDER_FIXTURE_1.setOrderedTime(LocalDateTime.of(2010, 1, 1, 1, 1));
        ORDER_FIXTURE_1.setOrderLineItems(Arrays.asList(ORDER_LINE_ITEM_FIXTURE_1));
        ORDER_FIXTURE_2.setOrderTableId(2L);
        ORDER_FIXTURE_2.setOrderStatus("MEAL");
        ORDER_FIXTURE_2.setOrderedTime(LocalDateTime.of(2020, 1, 1, 1, 2));
        ORDER_FIXTURE_2.setOrderLineItems(Arrays.asList(ORDER_LINE_ITEM_FIXTURE_2));
    }
}
