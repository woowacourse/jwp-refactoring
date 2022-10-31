package kitchenpos.table.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupSaveRequest {

    private final List<OrderTableIdDto> orderTables;

    public TableGroupSaveRequest(List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> toEntities() {
        return orderTables.stream()
            .map(OrderTableIdDto::getId)
            .collect(Collectors.toUnmodifiableList());
    }
}
