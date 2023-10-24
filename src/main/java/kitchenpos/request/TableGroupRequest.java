package kitchenpos.request;

import java.util.List;

public class TableGroupRequest {

    private final List<Long> tableIds;

    public TableGroupRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
