package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {}

    private TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<Long> orderTableIds) {
        List<OrderTableIdRequest> orderTableIdRequests = orderTableIds.stream()
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTableIdRequests);
    }

    public TableGroup toTableGroup() {
        List<OrderTable> orderTables = this.orderTables.stream()
                .map(OrderTableIdRequest::toOrderTable)
                .collect(Collectors.toList());
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    static class OrderTableIdRequest {
        private Long id;

        private OrderTableIdRequest() {}

        public OrderTableIdRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public OrderTable toOrderTable() {
            return new OrderTable(id);
        }
    }
}


