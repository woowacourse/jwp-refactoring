package kitchenpos.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupRequest {

    private final List<TableIdRequest> orderTables;

    @JsonCreator
    public TableGroupRequest(final List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
