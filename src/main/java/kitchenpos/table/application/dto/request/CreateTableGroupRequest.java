package kitchenpos.table.application.dto.request;

import java.util.List;

public class CreateTableGroupRequest {

    private List<TableInfo> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(List<TableInfo> orderTables) {
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
