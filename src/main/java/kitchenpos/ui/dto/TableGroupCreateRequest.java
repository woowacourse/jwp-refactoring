package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> tableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
