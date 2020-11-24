package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {
    private List<IdRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<IdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public List<IdRequest> getOrderTables() {
        return orderTables;
    }
}
