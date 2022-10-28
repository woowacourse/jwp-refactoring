package kitchenpos.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableGroupRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableGroupRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableGroupRequest {
        private Long id;

        private OrderTableGroupRequest() {
        }

        public OrderTableGroupRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
