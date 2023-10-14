package kitchenpos.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(toList());
    }

    public static class OrderTableIdRequest {
        private Long id;

        public OrderTableIdRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
