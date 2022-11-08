package kitchenpos.table.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<TableId> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        orderTables = orderTableIds.stream()
                .map(TableId::new)
                .collect(Collectors.toList());
    }

    public List<TableId> getOrderTables() {
        return orderTables;
    }

    public static class TableId {

        private Long id;

        public TableId() {
        }

        public TableId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
