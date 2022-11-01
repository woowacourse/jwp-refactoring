package kitchenpos.table.presentation.dto;

import java.util.List;
import kitchenpos.table.application.dto.request.OrderTableCommand;
import kitchenpos.table.application.dto.request.TableGroupCommand;

public record TableGroupRequest(List<OrderTableRequest> orderTables) {
    public TableGroupCommand toCommand() {
        List<OrderTableCommand> orderTableCommands = orderTables.stream()
                .map(OrderTableRequest::toCommand)
                .toList();
        return new TableGroupCommand(orderTableCommands);
    }
}
