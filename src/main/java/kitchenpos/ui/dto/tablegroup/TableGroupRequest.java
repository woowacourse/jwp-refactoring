package kitchenpos.ui.dto.tablegroup;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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
