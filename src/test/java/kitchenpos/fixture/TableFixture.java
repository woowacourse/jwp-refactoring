package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;

public class TableFixture {

    public static OrderTable 비어있는_주문_테이블() {
        return new OrderTable(null, 0, true);
    }

    public static OrderTable 비어있지_않는_주문_테이블() {
        return new OrderTable(null, 0, false);
    }

    public static List<OrderTable> 전체_주문_테이블() {
        return List.of(
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블()
        );
    }
}
