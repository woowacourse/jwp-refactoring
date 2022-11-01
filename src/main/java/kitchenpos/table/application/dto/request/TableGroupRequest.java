package kitchenpos.table.application.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableGroupRequest {

    private final List<OrderTableIdRequest> orderTables;

    @JsonCreator
    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
