package kitchenpos.tablegroup.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup() {
        List<OrderTable> orderTables = this.orderTables.stream()
                .map(OrderTableRequest::toOrderTable)
                .collect(Collectors.toList());
        return new TableGroup(null, LocalDateTime.now(), new OrderTables(orderTables));
    }
}
