package kitchenpos.application.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableId> orderTables;

    public TableGroupCreateRequest(final List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableId {

        private final Long id;

        public OrderTableId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
