package kitchenpos.ui.dto.tablegroup;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds = new ArrayList<>();

    protected TableGroupRequest() { }

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toEntity(OrderTables orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables.getOrderTables());
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
