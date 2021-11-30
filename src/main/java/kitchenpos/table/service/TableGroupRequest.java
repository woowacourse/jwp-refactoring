package kitchenpos.table.service;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableId> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableId {
        private Long id;

        public OrderTableId() {
        }

        public OrderTableId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
