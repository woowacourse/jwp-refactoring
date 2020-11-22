package kitchenpos.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private Set<TableId> orderTables;

    private TableGroupCreateRequest() {
    }

    private TableGroupCreateRequest(Set<TableId> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupCreateRequest of(List<Long> ids) {
        return new TableGroupCreateRequest(ids.stream().map(TableId::new).collect(Collectors.toSet()));
    }

    public Set<TableId> getOrderTables() {
        return orderTables;
    }

    public Set<Long> toTableIds() {
        return orderTables.stream().map(TableId::getId).collect(Collectors.toSet());
    }

    private static class TableId {
        private Long id;

        private TableId() {
        }

        public TableId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
