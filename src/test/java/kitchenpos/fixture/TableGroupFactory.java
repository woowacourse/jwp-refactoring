package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFactory {

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
