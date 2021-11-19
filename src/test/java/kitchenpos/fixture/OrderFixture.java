package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import static kitchenpos.fixture.OrderLineItemFixture.양념반_후라이드반_하나;
import static kitchenpos.fixture.OrderLineItemFixture.후라이드_단품_둘;
import static kitchenpos.fixture.OrderTableFixture.단일_손님2_테이블;

public class OrderFixture {
    public static Order COOKING_ORDER = new Order(
            1L,
            단일_손님2_테이블,
            OrderStatus.COOKING,
            LocalDateTime.now(),
            Arrays.asList(후라이드_단품_둘, 양념반_후라이드반_하나)
    );

    public static Order COMPLETION_ORDER = new Order(
            2L,
            단일_손님2_테이블,
            OrderStatus.COMPLETION,
            LocalDateTime.now(),
            Collections.singletonList(후라이드_단품_둘)
    );
}
