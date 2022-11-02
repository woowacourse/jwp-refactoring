package kitchenpos.tablegroup.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.request.OrderTableIdRequest;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableIdRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.stream()
            .map(OrderTableIdRequest::toEntity)
            .collect(Collectors.toList());
    }

    public TableGroup toEntity() {
        return new TableGroup(LocalDateTime.now());
    }
}
