package kitchenpos.ui.table.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import kitchenpos.application.table.dto.CreateTableGroupCommand;

public class CreateTableGroupRequest {

    @JsonProperty("orderTables")
    private List<Long> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public CreateTableGroupCommand toCommand() {
        return new CreateTableGroupCommand(orderTables);
    }
}
