package kitchenpos.menu.dto;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableOfGroupRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableOfGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableOfGroupRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableOfGroupRequest {

        private Long id;

        protected OrderTableOfGroupRequest() {
        }

        public OrderTableOfGroupRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
