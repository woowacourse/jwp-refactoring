package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeGuestRequest;
import kitchenpos.dto.OrderTableRequest;

public class OrderTableFixture {
    public static OrderTable orderTable() {
        return new OrderTable(0L, 0L, 0, false);
    }

    public static OrderTable notGroupedTable() {
        return new OrderTable(0L, null, 0, true);
    }

    public static OrderTableRequest orderTableRequest() {
        return new OrderTableRequest(0, false);
    }

    public static OrderTableChangeEmptyRequest orderEmptyRequest() {
        return new OrderTableChangeEmptyRequest(true);
    }

    public static OrderTableChangeGuestRequest orderGuestChangeRequest() {
        return new OrderTableChangeGuestRequest(0);
    }
}
