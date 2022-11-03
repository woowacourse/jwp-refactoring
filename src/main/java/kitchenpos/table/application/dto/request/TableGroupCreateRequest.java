package kitchenpos.table.application.dto.request;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<OrderTableIdRequest> orderTables;

    public TableGroupCreateRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.stream()
                .map(OrderTableIdRequest::toEntity)
                .collect(Collectors.toList());
    }

    public TableGroup toEntity() {
        return new TableGroup(LocalDateTime.now(), getOrderTables());
    }
}
