package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;

import java.util.List;

public class TableGroupFixture {

    public static TableGroup TABLE_GROUP(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
