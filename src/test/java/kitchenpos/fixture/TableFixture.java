package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;

public class TableFixture {

    public static OrderTable 주문_테이블() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }

    public static List<OrderTable> 전체_주문_테이블() {
        return List.of(
            주문_테이블(),
            주문_테이블(),
            주문_테이블(),
            주문_테이블(),
            주문_테이블(),
            주문_테이블(),
            주문_테이블(),
            주문_테이블()
        );
    }
}
