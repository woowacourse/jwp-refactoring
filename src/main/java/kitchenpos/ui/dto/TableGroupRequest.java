package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private final List<OrderTable> orderTables;

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
