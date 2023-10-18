package kitchenpos.table.application.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<TableInfo> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<TableInfo> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableInfo> getOrderTables() {
        return orderTables;
    }

    public static class TableInfo {

        private Long id;

        public TableInfo() {
        }

        public TableInfo(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
