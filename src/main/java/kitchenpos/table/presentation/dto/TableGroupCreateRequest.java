package kitchenpos.table.presentation.dto;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableIdDto;
import kitchenpos.table.application.dto.TableGroupSaveRequest;

public class TableGroupCreateRequest {

    private List<OrderTableIdDto> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupSaveRequest toRequest() {
        return new TableGroupSaveRequest(orderTables);
    }
}
