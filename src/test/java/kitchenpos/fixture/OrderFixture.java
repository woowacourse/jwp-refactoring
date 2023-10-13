package kitchenpos.fixture;

import kitchenpos.domain.Order;

import static java.time.LocalDateTime.now;

public class OrderFixture {

    public static Order 주문_상품_없음() {
        return new Order(1L, "COOKING", now(), null);
    }
}
