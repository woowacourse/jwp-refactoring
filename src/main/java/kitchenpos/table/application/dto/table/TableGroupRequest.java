package kitchenpos.table.application.dto.table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    List<OrderTableIdRequest> orderTables;

    @JsonCreator
    public TableGroupRequest(@JsonProperty("orderTables") final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables.stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toUnmodifiableList());
    }
}
