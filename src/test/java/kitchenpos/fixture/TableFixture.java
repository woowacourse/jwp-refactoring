package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;

public class TableFixture {

    public static OrderTableRequest createOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static TableGroup createTableGroup(OrderTable... orderTables) {
        return new TableGroup(List.of(orderTables));
    }
}
