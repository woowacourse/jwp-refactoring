package kitchenpos.fixture;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;

public class TableFixture {

    public static OrderTableRequest createOrderTableRequest(final int numberOfGuests, final boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static TableGroupRequest createTableGroupRequest(final OrderTable... orderTables) {
        return new TableGroupRequest(List.of(orderTables));
    }
}
