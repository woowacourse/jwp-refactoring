package support.fixture;

import kitchenpos.domain.order_table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupBuilder {

    private final TableGroup tableGroup;

    public TableGroupBuilder(final List<OrderTable> tables) {
        this.tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(tables);
    }

    public TableGroup build() {
        return tableGroup;
    }
}
