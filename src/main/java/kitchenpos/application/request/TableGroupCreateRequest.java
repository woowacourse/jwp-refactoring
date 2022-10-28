package kitchenpos.application.request;

import java.util.List;
import lombok.Getter;

@Getter
public class TableGroupCreateRequest {

    private List<OrderTableGroupCreateRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableGroupCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
