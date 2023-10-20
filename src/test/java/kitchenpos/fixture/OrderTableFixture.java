package kitchenpos.fixture;

import java.util.function.Function;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성(final Function<OrderTable, OrderTable> persistable) {
        return new OrderTable(0, false);
    }

    public static OrderTable 주문_테이블_생성() {
        return new OrderTable(0, false);
    }

    public static OrderTable 빈_테이블_생성(final Function<OrderTable, OrderTable> persistable) {
        return new OrderTable(0, true);
    }
    
    public static OrderTable 빈_테이블_생성() {
        return new OrderTable(0, true);
    }
}
