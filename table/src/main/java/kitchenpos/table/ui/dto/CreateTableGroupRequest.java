package kitchenpos.table.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.CreateTableGroupCommand;

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
