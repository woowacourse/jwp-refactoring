package kitchenpos.tablegroup.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(final List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(OrderTableRequest::new)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                TableGroupRequest::new
                        )
                );

    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
