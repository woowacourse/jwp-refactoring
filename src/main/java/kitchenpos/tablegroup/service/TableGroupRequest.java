package kitchenpos.tablegroup.service;

import java.util.List;

public class TableGroupRequest {
    private final List<OrderTableId> orderTables;

    public TableGroupRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableId {
        private final Long id;

        public OrderTableId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
