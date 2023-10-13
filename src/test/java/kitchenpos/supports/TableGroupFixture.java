package kitchenpos.supports;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup from(final OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(new ArrayList<>(List.of(orderTables)));
        return tableGroup;
    }
}
