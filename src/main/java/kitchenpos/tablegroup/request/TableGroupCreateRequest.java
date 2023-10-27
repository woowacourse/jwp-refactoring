package kitchenpos.tablegroup.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class TableGroupCreateRequest {

    @NotNull
    private final List<TableGroupUnitDto> orderTables;

    public TableGroupCreateRequest(List<TableGroupUnitDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupUnitDto> getOrderTables() {
        return orderTables;
    }
}
