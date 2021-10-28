package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;
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

    public static TableGroupRequest from(List<Long> orderTableIds) {
        List<OrderTableIdRequest> orderTableIdRequests = orderTableIds.stream()
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTableIdRequests);
    }

    public TableGroup toTableGroup() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

    }

    public static class OrderTableIdRequest {
        private Long id;

        private OrderTableIdRequest() {
        }

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


