package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private final List<Long> orderTables;

    private TableGroupResponse(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup){
        List<Long> tableIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        return new TableGroupResponse(tableIds);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
