package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public class TableGroupCreateRequest {

    @NotNull
    @JsonProperty(value = "orderTables")
    private List<TableGroupCreateWithTableRequest> tableRequests;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<TableGroupCreateWithTableRequest> tableRequests) {
        this.tableRequests = tableRequests;
    }

    public List<Long> getOrderTableIds() {
        return this.tableRequests.stream()
                .map(TableGroupCreateWithTableRequest::getId)
                .collect(Collectors.toList());
    }
}
