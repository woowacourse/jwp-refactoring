package kitchenpos.dto.table;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<Long> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables) {
        return TableGroup.of(this.orderTables, orderTables);
    }
}
