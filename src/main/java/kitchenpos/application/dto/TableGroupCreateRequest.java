package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<Long> orderTableIds;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables, LocalDateTime now) {
        return new TableGroup(
                now,
                orderTables
        );
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
