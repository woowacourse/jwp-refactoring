package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroup toTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                orderTables.stream()
                        .map(OrderTableRequest::toOrderTable)
                        .collect(Collectors.toList())
        );
        return tableGroup;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
