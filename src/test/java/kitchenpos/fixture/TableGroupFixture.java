package kitchenpos.fixture;

import java.util.List;
import java.util.function.Function;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 단체_테이블_저장(
            final Function<TableGroup, TableGroup> tableGroupPersistable,
            final List<OrderTable> orderTables
    ) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroupPersistable.apply(tableGroup);
    }
}
