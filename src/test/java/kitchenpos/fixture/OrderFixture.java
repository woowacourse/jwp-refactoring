package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 조리_상태의_주문() {
        return new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now());
    }

    public static Order 식사_상태의_주문() {
        return new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now());
    }

    public static Order 계산완료_상태의_주문() {
        return new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now());
    }
}
