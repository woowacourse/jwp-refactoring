package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFactory {

    public static TableGroup tableGroup(final OrderTable... orderTables) {
        return new TableGroup(null, null, List.of(orderTables));
    }
}
