package kitchenpos.ui.dto;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {
    }

    private TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }


    public static TableGroupRequest from(List<OrderTableIdRequest> orderTableIdRequests) {
        return new TableGroupRequest(orderTableIdRequests);
    }

    public TableGroup toTableGroup() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}


