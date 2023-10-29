package ordertable.test.java.kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;

public class TableFixture {

    public static OrderTable createTableById(final Long id) {
        return new OrderTable(id, null, 0, false);
    }

    public static OrderTable createTableByEmpty(final boolean empty) {
        return new OrderTable(null, 0, empty);
    }

    public static final OrderTable FILL_TABLE = new OrderTable(null, 0, false);
    public static final OrderTable EMPTY_TABLE = new OrderTable(null, 0, true);

    public static final OrderTableRequest EMPTY_TABLE_REQUEST = new OrderTableRequest(null, 0, true);

    public static final OrderTable TABLE = new OrderTable(1L, 6, false);
}
