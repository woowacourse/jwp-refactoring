package kitchenpos.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<Long> tableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
