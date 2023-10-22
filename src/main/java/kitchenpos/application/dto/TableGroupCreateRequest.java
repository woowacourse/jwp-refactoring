package kitchenpos.application.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {

        private Long id;

        public OrderTableRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
