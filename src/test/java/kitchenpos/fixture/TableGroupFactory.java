package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupFactory {

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
