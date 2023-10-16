package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupingRequest {
    private final List<GroupOrderTableRequest> orderTables;

    @JsonCreator
    public TableGroupingRequest(final List<GroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<GroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
