package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableIdDto> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup(final LocalDateTime createdDate) {
        return new TableGroup(null, createdDate);
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}
