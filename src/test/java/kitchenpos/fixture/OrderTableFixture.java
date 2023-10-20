package kitchenpos.fixture;

import java.util.function.Function;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문_테이블_저장(final Function<OrderTable, OrderTable> persistable) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);
        return persistable.apply(orderTable);
    }

    public static OrderTable 주문_테이블_생성() {
        return new OrderTable(0, false);
    }

    public static OrderTable 빈_테이블_저장(final Function<OrderTable, OrderTable> persistable) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        return persistable.apply(orderTable);
    }

    public static OrderTable 빈_테이블_생성() {
        return new OrderTable(0, true);
    }
}
