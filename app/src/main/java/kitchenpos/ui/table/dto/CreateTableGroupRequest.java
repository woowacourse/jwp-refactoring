package kitchenpos.ui.table.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.table.dto.CreateTableGroupCommand;

public class CreateTableGroupRequest {

    @JsonProperty("orderTables")
    private List<OrderTableIdRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public CreateTableGroupCommand toCommand() {
        return new CreateTableGroupCommand(orderTables.stream()
                .map(OrderTableIdRequest::id)
                .collect(Collectors.toList())
        );
    }
}
