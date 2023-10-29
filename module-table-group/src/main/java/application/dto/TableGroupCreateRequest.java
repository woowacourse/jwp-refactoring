package application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTables;

    @JsonCreator
    public TableGroupCreateRequest(@JsonProperty("orderTables") final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
