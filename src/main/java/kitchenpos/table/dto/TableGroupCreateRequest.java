package kitchenpos.table.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

public class TableGroupCreateRequest {

    @NotEmpty
    private Set<Long> tableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(@NotEmpty Set<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public Set<Long> getTableIds() {
        return tableIds;
    }
}
