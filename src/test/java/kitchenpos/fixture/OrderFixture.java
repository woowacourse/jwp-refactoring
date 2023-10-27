package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 조리_상태의_주문() {
        OrderTable 단일_신규_테이블 = OrderTableFixture.단일_신규_테이블();
        return new Order(1L, 단일_신규_테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
    }

    public static Order 식사_상태의_주문() {
        OrderTable 단일_신규_테이블 = OrderTableFixture.단일_신규_테이블();
        return new Order(1L, 단일_신규_테이블.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());
    }

    public static Order 계산완료_상태의_주문() {
        OrderTable 단일_신규_테이블 = OrderTableFixture.단일_신규_테이블();
        return new Order(1L, 단일_신규_테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now());
    }
}
