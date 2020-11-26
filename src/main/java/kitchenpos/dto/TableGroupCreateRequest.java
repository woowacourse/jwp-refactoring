package kitchenpos.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TableGroupCreateRequest {

    @NotEmpty
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
