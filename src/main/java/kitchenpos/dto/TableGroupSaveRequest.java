package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupSaveRequest {

    private List<OrderTable> orderTables;

    private TableGroupSaveRequest() {
    }

    public TableGroupSaveRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(null, orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
