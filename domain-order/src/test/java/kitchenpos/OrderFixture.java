package kitchenpos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import static kitchenpos.OrderLineItemFixture.양념반_후라이드반_하나;
import static kitchenpos.OrderLineItemFixture.후라이드_단품_둘;

public class OrderFixture {
    public static Order COOKING_ORDER = new Order(
            1L,
            1L,
            OrderStatus.COOKING,
            LocalDateTime.now(),
            Arrays.asList(후라이드_단품_둘, 양념반_후라이드반_하나)
    );

    public static Order COMPLETION_ORDER = new Order(
            2L,
            2L,
            OrderStatus.COMPLETION,
            LocalDateTime.now(),
            Collections.singletonList(후라이드_단품_둘)
    );
}
