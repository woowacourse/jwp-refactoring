package kitchenpos.api.tablegroup.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupsRequest {
    @JsonProperty("orderTables")
    private final List<TableGroupIdRequest> orderTables;

    @JsonCreator
    public TableGroupsRequest(final List<TableGroupIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables
                .stream().map(TableGroupIdRequest::getId)
                .collect(Collectors.toList());
    }
}
