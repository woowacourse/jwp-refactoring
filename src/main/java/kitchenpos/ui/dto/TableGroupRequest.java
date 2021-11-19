package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupRequest {

    private List<TableRequest> tableRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<TableRequest> tableRequests) {
        this.tableRequests = tableRequests;
    }

    public List<TableRequest> getTableRequests() {
        return tableRequests;
    }
}
