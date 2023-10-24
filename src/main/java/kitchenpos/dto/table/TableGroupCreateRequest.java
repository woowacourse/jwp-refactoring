package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables) {
        return TableGroup.of(getOrderTableIds(), orderTables);
    }
}
