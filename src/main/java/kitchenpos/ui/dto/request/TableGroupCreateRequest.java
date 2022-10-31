package kitchenpos.ui.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<TableCreateDto> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<TableCreateDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableCreateDto> getOrderTables() {
        return orderTables;
    }
}
