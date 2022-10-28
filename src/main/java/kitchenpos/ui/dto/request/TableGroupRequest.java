package kitchenpos.ui.dto.request;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCommand;

public class TableGroupRequest {

    private List<Long> tableTableGroupId;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> tableTableGroupId) {
        this.tableTableGroupId = tableTableGroupId;
    }

    public List<Long> getTableTableGroupId() {
        return tableTableGroupId;
    }

    public TableGroupCommand toCommand() {
        return new TableGroupCommand(tableTableGroupId);
    }
}
