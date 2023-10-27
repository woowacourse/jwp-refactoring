package kitchenpos.tablegroup.ui.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {

    private final List<OrderTableDto> orderTables;

    public TableGroupRequest(final List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity(final LocalDateTime now, final List<OrderTable> orderTables) {
        return TableGroup.of(now, orderTables);
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
