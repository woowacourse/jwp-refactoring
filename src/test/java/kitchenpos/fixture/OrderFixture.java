package kitchenpos.fixture;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_망고치킨_2개(final OrderStatus orderStatus) {
        return new Order(OrderTableFixture.주문테이블_N명(2), COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));
    }

    public static Order 주문_망고치킨_2개_빈주문항목(final OrderStatus orderStatus) {
        return new Order(OrderTableFixture.주문테이블_N명(2), COOKING.name(), LocalDateTime.now());
    }
}
