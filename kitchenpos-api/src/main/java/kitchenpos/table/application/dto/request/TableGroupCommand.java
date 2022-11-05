package kitchenpos.table.application.dto.request;

import java.util.List;

public record TableGroupCommand(List<OrderTableCommand> orderTables) {

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTableCommand::id)
                .toList();
    }
}
