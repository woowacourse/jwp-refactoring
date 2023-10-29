package kitchenpos.tablegroup.request;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public class TableGroupCreateRequest {

    @NotNull
    private final List<TableGroupUnitDto> orderTables;

    public TableGroupCreateRequest(List<TableGroupUnitDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(TableGroupUnitDto::getId)
                .collect(Collectors.toList());
    }

    public List<TableGroupUnitDto> getOrderTables() {
        return orderTables;
    }

}
