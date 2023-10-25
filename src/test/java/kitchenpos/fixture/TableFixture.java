package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;

public class TableFixture {

    public static OrderTable createTableById(final Long id) {
        return new OrderTable(id, null, 0, false);
    }

    public static OrderTable createTableByEmpty(final boolean empty) {
        return new OrderTable(null, 0, empty);
    }

    public static final OrderTable EMPTY_TABLE = new OrderTable(null, 0, false);

    public static final OrderTableRequest EMPTY_TABLE_REQUEST = new OrderTableRequest(null, 0, false);

    public static final OrderTable TABLE = new OrderTable(1L, 6, false);
}
