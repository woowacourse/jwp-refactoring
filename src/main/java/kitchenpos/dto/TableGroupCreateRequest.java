package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    List<Long> tableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
