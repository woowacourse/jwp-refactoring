package kitchenpos.ui.dto.request;

import java.util.List;

public class TableGroupCreateIds {
    private List<TableGroupCreateId> orderTables;

    public TableGroupCreateIds() {
    }

    public TableGroupCreateIds(final List<TableGroupCreateId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupCreateId> getOrderTables() {
        return orderTables;
    }
}
