package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

public class OrderFixtures {

    public static Order 주문1번() {
        OrderTable 주문테이블1번 = OrderTableFixtures.주문테이블1번();
        return new Order(1L, 주문테이블1번.getId());
    }

    public static Order 주문2번() {
        OrderTable 주문테이블2번 = OrderTableFixtures.주문테이블2번();
        return new Order(2L, 주문테이블2번.getId());
    }
}
