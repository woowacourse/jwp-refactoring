package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;

public class TableGroupFixture {

    public static TableGroup TABLE_GROUP(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
