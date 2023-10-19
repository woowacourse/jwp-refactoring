package kitchenpos.application.dto.tablegroup;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {

        private Long id;

        private OrderTableRequest() {
        }

        public OrderTableRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
