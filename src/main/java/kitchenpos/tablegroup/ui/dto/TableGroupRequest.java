package kitchenpos.tablegroup.ui.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {

    private final List<OrderTableDto> orderTables;

    public TableGroupRequest(final List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity(final LocalDateTime now) {
        return new TableGroup(now);
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
