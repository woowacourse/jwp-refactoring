package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixtures {

    public static OrderTable 주문테이블1번() {
        return new OrderTable(1L, null, 2, false);
    }

    public static OrderTable 주문테이블2번() {
        return new OrderTable(2L, null, 3, false);
    }

    public static OrderTable 빈테이블3번() {
        return new OrderTable(3L, null, 0, true);
    }

    public static OrderTable 빈테이블4번() {
        return new OrderTable(4L, null, 0, true);
    }
}
