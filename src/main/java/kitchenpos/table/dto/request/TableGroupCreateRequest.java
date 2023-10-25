package kitchenpos.table.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> tableIds;

    protected TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
