package kitchenpos.table.dto;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupSaveRequest {

    private List<OrderTable> orderTables;

    private TableGroupSaveRequest() {
    }

    public TableGroupSaveRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
