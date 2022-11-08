package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupRequest {

    private List<TableGroupInnerOrderTable> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<TableGroupInnerOrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupInnerOrderTable> getOrderTables() {
        return orderTables;
    }

    public static class TableGroupInnerOrderTable {

        private Long id;

        private TableGroupInnerOrderTable() {
        }

        public TableGroupInnerOrderTable(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
